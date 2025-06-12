package sprideapp.spride.member.auth.kakao;

import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sprideapp.spride.exception.SignupRequiredException;
import sprideapp.spride.member.Member;
import sprideapp.spride.member.MemberRepository;
import sprideapp.spride.member.auth.kakao.dto.*;
import sprideapp.spride.security.JwtProvider;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class KakaoService {

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${app.default-profile}")
    private String defaultProfile;



    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;


    private static final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
    private static final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com";


    private String getAccessTokenFromKakao(String code) {
        KakaoTokenResponse kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoTokenResponse.class)
                .block();


        log.info(" [getAccessTokenFromKakao] Access Token ------> {}", kakaoTokenResponseDto.getAccessToken());
        log.info(" [getAccessTokenFromKakao] Refresh Token ------> {}", kakaoTokenResponseDto.getRefreshToken());
        return kakaoTokenResponseDto.getAccessToken();
    }


    private KakaoUserInfoResponse getUserInfo(String accessToken) {
        KakaoUserInfoResponse userInfo = WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoUserInfoResponse.class)
                .block();

        //기본 프사라면 우리의 기본 프사를 등록
        if (userInfo.getKakaoAccount().getProfile().getIsDefaultImage()) {
            userInfo.getKakaoAccount().getProfile().setProfileImageUrl(defaultProfile);
        }
        log.info("[ getUserInfo ] Auth ID ---> {} ", userInfo.getId());
        log.info("[ getUserInfo ] NickName ---> {} ", userInfo.getKakaoAccount().getProfile().getNickName());
        log.info("[ getUserInfo ] ProfileImageUrl ---> {} ", userInfo.getKakaoAccount().getProfile().getProfileImageUrl());

        return userInfo;
    }

    //로그인
    @Transactional(readOnly = true)
    public LoginResult login(String code) {
        log.info("==============================={}",code);
        String kakaoAccessToken = getAccessTokenFromKakao(code);
        KakaoUserInfoResponse userInfo = getUserInfo(kakaoAccessToken);

        Long kakaoId = userInfo.getId();

        Optional<Member> existingMember = memberRepository.findByKakaoId(kakaoId);
        if (existingMember.isPresent()) { //기존 회원
            Member member = existingMember.get();
            String accessToken = jwtProvider.createAccessToken(member.getId());
            String refreshToken = jwtProvider.createAccessToken(member.getId());

            LoginResponse response = LoginResponse.from(member/*, accessToken*/);

            return new LoginResult(accessToken, refreshToken, response);
        } else { //회원 가입 필요
            String temporaryToken = jwtProvider.createTemporaryToken(kakaoId);
            throw new SignupRequiredException(temporaryToken,
                    userInfo.getKakaoAccount().getProfile().getNickName(),
                    userInfo.getKakaoAccount().getProfile().getProfileImageUrl()
            );
        }
    }


    //회원가입
    public LoginResult signup(KakaoSignupRequest request, Long kakaoId) {
        Member member = memberRepository.save(request.toEntity(kakaoId));

        String accessToken = jwtProvider.createAccessToken(member.getId());
        String refreshToken = jwtProvider.createAccessToken(member.getId());
        LoginResponse response = LoginResponse.from(member/*, accessToken*/);

        return new LoginResult(accessToken, refreshToken, response);
    }

    //테스트용입니다
    public String test() {
        Member member = Member.builder()
                .kakaoId(4291667192L)
                .introText("안녕하십니까!!")
                .nickname("테스트용")
                .profileUrl("https://api.wownd.store/images/profile/default_image.jpg")
                .build();
        Member save = memberRepository.save(member);
        return jwtProvider.createAccessToken(save.getId());
    }

}