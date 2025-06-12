package sprideapp.spride.shuttle;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sprideapp.spride.shuttle.dto.ShuttleInfoRequest;
import sprideapp.spride.shuttle.dto.ShuttleInfoResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shuttles")
public class ShuttleApiController {
    private final ShuttleService shuttleService;

    @GetMapping
    public List<ShuttleInfoResponse> getInfo(@RequestParam List<Long> idList) {
        return shuttleService.getInfo(idList);
    }
}
