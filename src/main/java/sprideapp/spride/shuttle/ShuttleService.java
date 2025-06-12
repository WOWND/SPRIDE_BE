package sprideapp.spride.shuttle;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sprideapp.spride.shuttle.dto.ShuttleInfoRequest;
import sprideapp.spride.shuttle.dto.ShuttleInfoResponse;
import sprideapp.spride.shuttlelog.ShuttleLog;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class ShuttleService {
    private final ShuttleRepository shuttleRepository;

    public void saveShuttle(Shuttle shuttle) {
        shuttleRepository.save(shuttle);
    }

    @Transactional(readOnly = true)
    public List<ShuttleInfoResponse> getInfo(List<Long> idList) {
        //List<Long> idList = request.getIdList();

        log.info("==========================={},{},{}====", idList.get(0), idList.get(1), idList.get(2));
        List<ShuttleInfoResponse> response = new ArrayList<>();
        for (Long shuttleId : idList) {
            Shuttle shuttle = getShuttle(shuttleId);
            List<ShuttleLog> shuttleLogs = shuttle.getShuttleLogs();
            if (!shuttleLogs.isEmpty()) {
                ShuttleLog shuttleLog = shuttleLogs.get(shuttleLogs.size() - 1);
                response.add(ShuttleInfoResponse.builder()
                        .id(shuttleId)
                        .crowdLevel(shuttleLog.getCrowdLevel())
                        .status(shuttleLog.getStatus())
                        .time(shuttleLog.getTime())
                        .build());
            }
        }
        return response;
    }


    private Shuttle getShuttle(Long shuttleId) {
        return shuttleRepository.findById(shuttleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID입니다"));
    }
}
