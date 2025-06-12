package sprideapp.spride.comment;

import jakarta.persistence.*;
import lombok.*;
import sprideapp.spride.article.Article;
import sprideapp.spride.member.Member;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;
    private LocalTime time;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    @Builder
    public Comment(LocalTime time, String content, Member member, Article article) {
        this.time = time;
        this.content = content;
        this.member = member;
        this.article = article;
    }
}
