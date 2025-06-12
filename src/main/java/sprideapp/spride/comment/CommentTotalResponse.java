package sprideapp.spride.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CommentTotalResponse {
    public String nickname;
    public String content;
    public LocalTime time;
    public List<CommentResponse> comments;
}
