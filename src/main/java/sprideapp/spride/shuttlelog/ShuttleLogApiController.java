package sprideapp.spride.shuttlelog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sprideapp.spride.shuttle.ShuttleService;
import sprideapp.spride.shuttlelog.dto.ShuttleLogRequest;
import sprideapp.spride.shuttlelog.dto.ShuttleLogResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shuttles/{shuttleId}/logs")
@Slf4j
public class ShuttleLogApiController {
    private final ShuttleService shuttleService;
    private final ShuttleLogService shuttleLogService;


    @GetMapping
    public List<ShuttleLogResponse> getLogs(@PathVariable Long shuttleId) {
        return shuttleLogService.getLogs(shuttleId);
    }

    @PostMapping
    public ShuttleLogResponse createLog(@PathVariable Long shuttleId, @RequestBody ShuttleLogRequest request,
                          @AuthenticationPrincipal Long memberId) {
        log.info("===========셔틀아이디{}", shuttleId);
        log.info("===========회원아이디{}", memberId);
        return shuttleLogService.saveLog(shuttleId, request, memberId);
    }

}
