package sprideapp.spride.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberProfileResponse {
    public String nickname;
    public int score;
    public String profileUrl;
    public Level level;

    public static MemberProfileResponse from(Member member) {
        return new MemberProfileResponse(
                member.getNickname(),
                member.getScore(),
                member.getProfileUrl(),
                member.getLevel()
        );
    }
}
