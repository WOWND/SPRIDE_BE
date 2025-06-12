package sprideapp.spride.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sprideapp.spride.member.Level;
import sprideapp.spride.member.Member;

@Getter
@AllArgsConstructor
public class MemberProfileResponse {
    public String nickname;
    public int score;
    public String profileUrl;
    public Level level;
    public String introText;

    public static MemberProfileResponse from(Member member) {
        return new MemberProfileResponse(
                member.getNickname(),
                member.getScore(),
                member.getProfileUrl(),
                member.getLevel(),
                member.getIntroText()
        );
    }
}
