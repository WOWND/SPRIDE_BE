package sprideapp.spride.shuttlelog;


import jakarta.persistence.*;
import lombok.*;
import sprideapp.spride.member.Member;
import sprideapp.spride.shuttle.Shuttle;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShuttleLog {
    @Id
    @GeneratedValue
    @Column(name = "shuttle_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="shuttle_id")
    private Shuttle shuttle;

    @Enumerated(EnumType.STRING)
    private CrowdLevel crowdLevel;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalTime time;

    @Builder
    public ShuttleLog(Shuttle shuttle,Member member, CrowdLevel crowdLevel, Status status, LocalTime time) {
        this.shuttle = shuttle;
        this.member = member;
        this.crowdLevel = crowdLevel;
        this.status = status;
        this.time = time;
    }
}
