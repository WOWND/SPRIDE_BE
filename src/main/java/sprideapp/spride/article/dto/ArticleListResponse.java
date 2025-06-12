package sprideapp.spride.article.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import sprideapp.spride.article.Article;
import sprideapp.spride.shuttle.ShuttleDirection;
import sprideapp.spride.shuttle.ShuttleRoute;

import java.time.LocalTime;

@Getter
@Setter
@Builder
public class ArticleListResponse {

    public Long id;
    public ShuttleRoute route;
    public ShuttleDirection direction;
    @JsonFormat(pattern = "HH:mm:ss")
    public LocalTime time;


    public String imageUrl;
    public String title;

    public static ArticleListResponse fromTaxi(Article article) {
        return ArticleListResponse.builder()
                .id(article.getId())
                .route(article.getRoute())
                .direction(article.getDirection())
                .time(article.getTime())
                .build();
    }

    public static ArticleListResponse fromLost(Article article) {
        return ArticleListResponse.builder()
                .id(article.getId())
                .route(article.getRoute())
                .direction(article.getDirection())
                .title(article.getTitle())
                .time(article.getTime())
                .imageUrl(article.getImageUrl())
                .build();
    }
}
