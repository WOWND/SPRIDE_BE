package sprideapp.spride.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import sprideapp.spride.member.auth.kakao.dto.SignupInitResponse;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(HttpServletRequest request, IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 400,
                "error", "Bad Request",
                "message", e.getMessage(),
                "path", request.getRequestURI()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(HttpServletRequest request, Exception e) {
        return ResponseEntity.status(500).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 500,
                "error", "Internal Server Error",
                "message", e.getMessage(),
                "path", request.getRequestURI()
        ));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleFileSizeException(MaxUploadSizeExceededException e) {
        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("파일 크기가 너무 큽니다. 최대 5MB까지 허용됩니다.");
    }

    @ExceptionHandler(SignupRequiredException.class)
    public ResponseEntity<SignupInitResponse> handleGeneralException(HttpServletRequest request, SignupRequiredException e) {
        String cookie = getTemporaryCookie(e.getTemporaryToken());
        log.info("=================={}", cookie);

        return ResponseEntity.status(200)
                .header(HttpHeaders.SET_COOKIE, cookie)
                .body(new SignupInitResponse(e));
    }



    private String getTemporaryCookie(String temporaryToken) {
        return ResponseCookie.from("accessToken", temporaryToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(60 * 5) //5분
                .build()
                .toString();
    }
}
