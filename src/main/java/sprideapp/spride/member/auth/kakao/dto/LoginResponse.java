package sprideapp.spride.member.auth.kakao.dto;


import lombok.Builder;
import lombok.Getter;
import sprideapp.spride.member.Level;
import sprideapp.spride.member.Member;

@Getter
@Builder
public class LoginResponse {
    private String status = "LOGIN_SUCCESS";
    private String nickname;
    private String profileUrl;
    private String introText;
    private int score;
    private Level level;

    public static LoginResponse from(Member member) {
        return LoginResponse.builder()
                .nickname(member.getNickname())
                .profileUrl(member.getProfileUrl())
                .introText(member.getIntroText())
                .score(member.getScore())
                .level(member.getLevel())
                .build();
    }
}

