package sprideapp.spride.member;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private String introText;


    @Builder
    public Member(String nickname, String profileUrl, String introText, Long kakaoId) {
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.introText = introText;
        this.kakaoId = kakaoId;
        this.level = Level.BRONZE;
    }
}
