package sprideapp.spride.article.dto;

import lombok.Getter;
import lombok.Setter;
import sprideapp.spride.article.Article;
import sprideapp.spride.article.ArticleType;
import sprideapp.spride.member.Member;
import sprideapp.spride.shuttle.ShuttleDirection;
import sprideapp.spride.shuttle.ShuttleRoute;

import java.time.LocalTime;

@Getter
@Setter
public class CreateArticleRequest {
    public String title;
    public String content;
    public String imageUrl;
    public ShuttleRoute route;
    public ShuttleDirection direction;
    public ArticleType type;

    public Article toEntity(Member member) {
        return Article.builder()
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .route(route)
                .direction(direction)
                .type(type)
                .member(member)
                .time(LocalTime.now().withNano(0))
                .build();
    }

}
