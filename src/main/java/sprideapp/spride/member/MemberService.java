package sprideapp.spride.member;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;



    //프로필 조회
    public MemberProfileResponse getProfile(Long memberId) {
        Member member = findById(memberId);
        return MemberProfileResponse.from(member);
    }

    private Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다: id=" + memberId));
    }
}
