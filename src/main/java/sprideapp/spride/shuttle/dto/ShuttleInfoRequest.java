package sprideapp.spride.shuttle.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShuttleInfoRequest {
    List<Long> idList;
}
