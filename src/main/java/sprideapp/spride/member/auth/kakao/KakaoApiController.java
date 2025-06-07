package sprideapp.spride.member.auth.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import sprideapp.spride.member.auth.kakao.dto.KakaoSignupRequest;
import sprideapp.spride.member.auth.kakao.dto.LoginResponse;
import sprideapp.spride.member.auth.kakao.dto.LoginResult;

import java.net.URI;

@RequestMapping("/api/auth/kakao")
@RequiredArgsConstructor
@RestController
public class KakaoApiController {

    private final KakaoService kakaoService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestParam String code) {
        LoginResult result = kakaoService.login(code);

        return ResponseEntity.status(200)
                .header(HttpHeaders.SET_COOKIE,
                        getAccessCookie(result.accessToken),
                        getRefreshCookie(result.refreshToken))
                .body(result.getLoginResponse());
    }


    @PostMapping("/signup")
    public ResponseEntity<LoginResponse> kakaoSignup(@RequestBody KakaoSignupRequest request,
                                                     @AuthenticationPrincipal Long kakaoId) {
        LoginResult result = kakaoService.signup(request, kakaoId);
        return ResponseEntity.status(200)
                .header(HttpHeaders.SET_COOKIE,
                        getAccessCookie(result.accessToken),
                        getRefreshCookie(result.refreshToken))
                .body(result.getLoginResponse());
    }

    private String getAccessCookie(String accessToken) {
        return ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60) //1시간
                .build()
                .toString();
    }
    private String getRefreshCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60 * 24 * 14) //14일
                .build()
                .toString();
    }
}


//https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=8b92984a001a2c6e01bcbddd6081a7cb&redirect_uri=http://192.168.0.36:5173/auth/kakao/callback
