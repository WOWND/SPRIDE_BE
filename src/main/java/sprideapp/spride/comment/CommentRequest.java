package sprideapp.spride.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import sprideapp.spride.article.Article;
import sprideapp.spride.article.dto.ArticleListResponse;
import sprideapp.spride.member.Member;

import java.time.LocalTime;

@Getter
@Setter
public class CommentRequest {
    public String content;

    public Comment toEntity(Member member,Article article) {
        return Comment.builder()
                .content(content)
                .member(member)
                .article(article)
                .time(LocalTime.now().withNano(0))
                .build();
    }

}
