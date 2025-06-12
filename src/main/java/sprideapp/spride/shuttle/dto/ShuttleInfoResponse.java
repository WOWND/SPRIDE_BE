package sprideapp.spride.shuttle.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import sprideapp.spride.shuttlelog.CrowdLevel;
import sprideapp.spride.shuttlelog.Status;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ShuttleInfoResponse {
    public Long id;
    public CrowdLevel crowdLevel;
    public Status status;
    @JsonFormat(pattern = "HH:mm:ss")
    public LocalTime time;
}
