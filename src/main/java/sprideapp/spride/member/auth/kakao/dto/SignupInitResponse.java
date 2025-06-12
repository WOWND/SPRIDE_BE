package sprideapp.spride.member.auth.kakao.dto;

import lombok.Getter;
import lombok.Setter;
import sprideapp.spride.exception.SignupRequiredException;

@Getter
@Setter
public class SignupInitResponse {
    public String accessToken;
    public String status;
    public String nickname;
    public String profileUrl;

    public SignupInitResponse(SignupRequiredException e) {
        this.accessToken = e.getTemporaryToken();
        this.status = e.getStatus();
        this.nickname = e.getNickname();
        this.profileUrl = e.getProfileUrl();
    }
}
