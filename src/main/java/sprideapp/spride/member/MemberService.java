package sprideapp.spride.member;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sprideapp.spride.member.dto.MemberProfileResponse;
import sprideapp.spride.member.dto.UpdateProfileRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    @Value("${app.domain}")
    private String serverUrl;

    @Value("${app.default-profile}")
    public String defaultProfile;


    @Transactional(readOnly = true)
    public MemberProfileResponse getProfile(Long memberId) {
        Member member = findById(memberId);
        return MemberProfileResponse.from(member);
    }

    public String uploadProfileImage(Long memberId, MultipartFile file) {
        Member member = findById(memberId);

        //기존 이미지 url
        String currentUrl = member.getProfileUrl();

        // 새 프로필 업로드
        String uploadDir = "uploads/profile/";
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.lastIndexOf('.') != -1) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
        }
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + (extension.isEmpty() ? "" : "." + extension);

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }

        String imageUrl = serverUrl + "/images/profile/" + fileName;
        member.uploadProfileImage(imageUrl);

        // 기존 프로필 이미지가 기본 프로필이 아니면 삭제
        if (currentUrl != null && !currentUrl.contains("default")) {
            String oldFileName = currentUrl.substring(currentUrl.lastIndexOf('/') + 1);
            Path oldFilePath = Paths.get("uploads/profile/").resolve(oldFileName);
            try {
                Files.deleteIfExists(oldFilePath);
            } catch (IOException e) {
                log.warn("이전 프로필 이미지 삭제 실패: {}", oldFilePath, e);
            }
        }

        return imageUrl;
    }

    public void updateProfile(UpdateProfileRequest request,Long memberId) {
        Member member = findById(memberId);
        member.setNickname(request.nickname);
        member.setIntroText(request.introText);
    }

    @Transactional(readOnly = true)
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다: id=" + memberId));
    }

    //활동로그 추가하기
    public void saveActivity() {
    }



}
