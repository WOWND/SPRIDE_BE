package sprideapp.spride.member.auth.kakao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import sprideapp.spride.member.MemberService;
import sprideapp.spride.member.auth.kakao.dto.KakaoSignupRequest;
import sprideapp.spride.member.auth.kakao.dto.LoginResponse;
import sprideapp.spride.member.auth.kakao.dto.LoginResult;

import java.net.URI;

@RequestMapping("/api/auth/kakao")
@RequiredArgsConstructor
@RestController
@Slf4j
public class KakaoApiController {

    private final KakaoService kakaoService;
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestParam String code) {
        log.info("====================로그인요청발생");
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

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal Long kakaoId) {

        String expiredAccessToken = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(false)       // HTTPS 환경이라면 true로!
                .path("/")           // 생성 시와 동일한 path
                .maxAge(0)           // 0초 → 즉시 만료
                .build()
                .toString();

        String expiredRefreshToken = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)       // HTTPS 환경이라면 true로!
                .path("/")           // 생성 시와 동일한 path
                .maxAge(0)           // 0초 → 즉시 만료
                .build()
                .toString();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE,
                        expiredAccessToken,
                        expiredRefreshToken)
                .build();
    }



    //로그인 확인
    @GetMapping("/me")
    public ResponseEntity<Void> kakaoSignup(@AuthenticationPrincipal Long memberId) {
        return ResponseEntity.status(200).build();
    }


    private String getAccessCookie(String accessToken) {
        return ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(60 * 60) //1시간
                .build()
                .toString();
    }
    private String getRefreshCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(60 * 60 * 24 * 14) //14일
                .build()
                .toString();
    }
}


//https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=8b92984a001a2c6e01bcbddd6081a7cb&redirect_uri=http://192.168.0.36:5173/auth/kakao/callback
//http://localhost:8080/api/auth/kakao/login?code=
//192.168.0.36:5173/auth/kakao/callback?code=kNeKRVYP-CTeEUOexCJVQLejjLg6rQGWyGcV9RA5yUmKoDgC6kW-swAAAAQKFxJVAAABl1Bwx4mt1856Xp2T3g