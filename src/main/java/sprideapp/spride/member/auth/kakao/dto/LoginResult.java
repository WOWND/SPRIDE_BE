package sprideapp.spride.member.auth.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResult {
    public String accessToken;
    public String refreshToken;
    public LoginResponse loginResponse;
}
