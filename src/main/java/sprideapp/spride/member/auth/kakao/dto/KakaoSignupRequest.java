package sprideapp.spride.member.auth.kakao.dto;

import lombok.Getter;
import lombok.Setter;
import sprideapp.spride.member.Member;

@Getter
@Setter
public class KakaoSignupRequest {
    private String nickname;
    private String profileUrl;
    private String introText;

    //카카오
    public Member toEntity(Long kakaoId) {
        return Member.builder()
                .kakaoId(kakaoId)
                .nickname(this.nickname)
                .profileUrl(this.profileUrl)
                .introText(this.introText)
                .build();
    }
}
