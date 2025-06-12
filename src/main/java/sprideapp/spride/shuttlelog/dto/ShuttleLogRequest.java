package sprideapp.spride.shuttlelog.dto;

import lombok.Getter;
import lombok.Setter;
import sprideapp.spride.member.Member;
import sprideapp.spride.shuttle.Shuttle;
import sprideapp.spride.shuttlelog.CrowdLevel;
import sprideapp.spride.shuttlelog.ShuttleLog;
import sprideapp.spride.shuttlelog.Status;

import java.time.LocalTime;

@Getter
@Setter
public class ShuttleLogRequest {
    public CrowdLevel crowdLevel;
    public Status status;


    public ShuttleLog toEntity(Shuttle shuttle, Member member) {
        return ShuttleLog.builder()
                .shuttle(shuttle)
                .member(member)
                .crowdLevel(this.crowdLevel)
                .status(this.status)
                .time(LocalTime.now().withNano(0))
                .build();
    }
}
