package sprideapp.spride.shuttle;

import jakarta.persistence.*;
import lombok.*;
import sprideapp.spride.shuttlelog.ShuttleLog;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shuttle {
    @Id
    @GeneratedValue
    @Column(name = "shuttle_id")
    private Long id;
    private LocalTime departureTime;

    @Enumerated(EnumType.STRING)
    private ShuttleRoute routeName;

    @Enumerated(EnumType.STRING)
    private ShuttleDirection shuttleDirection;


    //private boolean isActive; //셔틀 운영 여부


    @OneToMany(mappedBy = "shuttle")
    private List<ShuttleLog> shuttleLogs = new ArrayList<>();

    @Builder
    public Shuttle(ShuttleRoute routeName, LocalTime departureTime, ShuttleDirection shuttleDirection) {
        this.routeName = routeName;
        this.departureTime = departureTime;
        this.shuttleDirection = shuttleDirection;
    }
}
