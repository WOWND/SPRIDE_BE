package sprideapp.spride.member;


import jakarta.persistence.*;
import lombok.*;
import sprideapp.spride.article.Article;
import sprideapp.spride.comment.Comment;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Article> articles = new ArrayList<>();


    @Builder
    public Member(String nickname, String profileUrl, String introText, Long kakaoId) {
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.introText = introText;
        this.kakaoId = kakaoId;
        this.level = Level.BRONZE;
    }

    public void uploadProfileImage(String imageUrl) {
        this.profileUrl = imageUrl;
    }
}
