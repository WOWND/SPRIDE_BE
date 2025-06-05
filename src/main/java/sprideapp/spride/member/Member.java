package sprideapp.spride.member;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String nickname;
    private int score;
    private String profileUrl;
    @Enumerated(EnumType.STRING)
    private Level level;
    private Long kakaoId;
}
