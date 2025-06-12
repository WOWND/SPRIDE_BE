package sprideapp.spride.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sprideapp.spride.member.dto.MemberProfileResponse;
import sprideapp.spride.member.dto.UpdateProfileRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberProfileResponse> getProfile(@AuthenticationPrincipal Long memberId) {
        MemberProfileResponse profile = memberService.getProfile(memberId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    public ResponseEntity<Void> updateProfile(@RequestBody UpdateProfileRequest request, @AuthenticationPrincipal Long memberId) {
        memberService.updateProfile(request,memberId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/profile-image")
    public ResponseEntity<Map<String,String>> uploadProfileImage(@RequestParam("profileImage") MultipartFile file,
                                                                 @AuthenticationPrincipal Long memberId) {
        String imageUrl = memberService.uploadProfileImage(memberId, file);
        Map<String, String> response = new HashMap<>();
        response.put("profileUrl", imageUrl);
        return ResponseEntity.ok().body(response);
    }

}