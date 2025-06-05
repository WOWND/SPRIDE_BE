package sprideapp.spride.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

    private final Key key;

    private final long TEMPORARY_TOKEN_EXPIRE_TIME = 1000 * 60 * 5 ; // 5분
    private final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 ; // 60분
    private final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 14; // 14일



    public JwtProvider(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }


    // Access Token 생성
    public String createAccessToken(Long memberId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .claim("type", "access")
                .setSubject(String.valueOf(memberId))
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Refresh Token 생성
    public String createRefreshToken(Long memberId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .claim("type", "refresh") //리프레시
                .setIssuedAt(now)
                .setSubject(String.valueOf(memberId))
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 카카오 ID 기반 임시 토큰 생성
    public String createTemporaryToken(Long kakaoId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + TEMPORARY_TOKEN_EXPIRE_TIME); // 5분 유효

        return Jwts.builder()
                .claim("type", "temp") // 타입은 구분을 위해 "temp"
                .setSubject(String.valueOf(kakaoId))
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token, String expectedType) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String tokenType = claims.get("type", String.class);
            if (!expectedType.equals(tokenType)) {
                log.warn("토큰 타입이 일치하지 않습니다. 예상: {}, 실제: {}", expectedType, tokenType);
                return false;
            }

            return true;

        } catch (SecurityException | MalformedJwtException e) {
            log.warn("잘못된 토큰입니다.");
        } catch (ExpiredJwtException e) {
            log.warn("만료된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.warn("지원하지 않는 형식의 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.warn("토큰이 비었습니다.");
        }

        return false;
    }

    // 토큰에서 memberId 추출
    public Long getIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    // 토큰에서 type 클레임 추출
    public String getTokenType(String token) {
        log.info("{}", token);
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("type", String.class);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("유효하지 않은 토큰: {}", e.getMessage());
            return null;
        }
    }
}
