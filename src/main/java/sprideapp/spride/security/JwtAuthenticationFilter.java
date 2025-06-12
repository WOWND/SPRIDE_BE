package sprideapp.spride.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();


        if (path.startsWith("/swagger-ui") ||
                path.endsWith("/login") || path.startsWith("/images/") ||
                path.endsWith("/test") || (path.startsWith("/api/shuttles") && method.equals("GET"))||
                (path.startsWith("/api/articles") && method.equals("GET"))||
                (path.startsWith("/api/comments") && method.equals("GET"))) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getTokenFromRequest(request);
        log.info("요청 받은 토큰{}", token);



        if (path.equals("/api/auth/kakao/me")) {
            if (token == null || !"access".equals(jwtProvider.getTokenType(token)) || !jwtProvider.validateToken(token, "access")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Access token이 없거나 만료되었습니다.");
                return;
            }
            filterChain.doFilter(request, response);
            return;
        }


        //회원가입은 임시토큰
        if (path.equals("/api/auth/kakao/signup")) {
            if (token == null || !"temp".equals(jwtProvider.getTokenType(token)) || !jwtProvider.validateToken(token, "temp")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("임시 토큰으로만 접근할 수 있습니다.");
                return;
            }
            Long kakaoId = jwtProvider.getIdFromToken(token);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(kakaoId, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
            return;
        }

        //나머지 api는 엑세스 토큰으로 접근
        if (token != null && jwtProvider.validateToken(token, "access")) {
            Long memberId = jwtProvider.getIdFromToken(token);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(memberId, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

//    private String getTokenFromRequest(HttpServletRequest request) {
//        String bearer = request.getHeader("Authorization");
//
//        if (bearer != null && bearer.startsWith("Bearer ")) {
//            return bearer.substring(7);
//        }
//        return null;
//    }

    //쿠키에서 토큰 꺼내기
    private String getTokenFromRequest(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


}
