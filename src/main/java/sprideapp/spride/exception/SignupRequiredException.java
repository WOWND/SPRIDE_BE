package sprideapp.spride.exception;

import lombok.Getter;

@Getter
public class SignupRequiredException extends RuntimeException {
    private final String status = "SIGNUP_REQUIRED";
    private final String temporaryToken;
    private final String nickname;
    private final String profileUrl;

    public SignupRequiredException(String temporaryToken,String nickname, String profileUrl) {
        super("Signup required");
        this.temporaryToken = temporaryToken;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
    }
}
