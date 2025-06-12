package sprideapp.spride.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import sprideapp.spride.article.Article;
import sprideapp.spride.article.dto.ArticleListResponse;

import java.time.LocalTime;

@Getter
@Setter
@Builder
public class CommentResponse {
    public String nickname;
    @JsonFormat(pattern = "HH:mm:ss")
    public LocalTime time;
    public String content;

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .nickname(comment.getMember().getNickname())
                .content(comment.getContent())
                .time(comment.getTime())
                .build();
    }
}
