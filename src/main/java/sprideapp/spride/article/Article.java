package sprideapp.spride.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import sprideapp.spride.comment.Comment;
import sprideapp.spride.member.Member;
import sprideapp.spride.shuttle.Shuttle;
import sprideapp.spride.shuttle.ShuttleDirection;
import sprideapp.spride.shuttle.ShuttleRoute;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {
    @Id
    @GeneratedValue
    @Column(name = "article_id")
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    private ShuttleRoute route;
    @Enumerated(EnumType.STRING)
    private ShuttleDirection direction;
    @Enumerated(EnumType.STRING)
    private ArticleType type;
    private LocalTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;


    @OneToMany(mappedBy = "article")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Article(String title, String content, String imageUrl, ShuttleRoute route, ShuttleDirection direction,
                   ArticleType type, Member member, LocalTime time) {

        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.route = route;
        this.direction = direction;
        this.type = type;
        this.member = member;
        this.time = time;
    }
}
