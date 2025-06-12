package sprideapp.spride.shuttlelog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import sprideapp.spride.shuttlelog.CrowdLevel;
import sprideapp.spride.shuttlelog.ShuttleLog;
import sprideapp.spride.shuttlelog.Status;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class ShuttleLogResponse {
    public Long id;
    public String nickname;
    public CrowdLevel crowdLevel;
    public Status status;
    @JsonFormat(pattern = "HH:mm:ss")
    public LocalTime time;

    public static ShuttleLogResponse from(ShuttleLog log) {
        return new ShuttleLogResponse(log.getId()
                , log.getMember().getNickname()
                , log.getCrowdLevel()
                , log.getStatus()
                , log.getTime());
    }
}
