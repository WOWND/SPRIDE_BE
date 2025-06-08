package sprideapp.spride.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequest {
    public String nickname;
    public String introText;
}
