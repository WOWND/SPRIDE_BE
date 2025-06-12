package sprideapp.spride.comment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/comments")
@RequiredArgsConstructor
@RestController
public class CommentApiController {
    private final CommentService commentService;

    @PostMapping("/{articleId}")
    public ResponseEntity<Long> create(@RequestBody CommentRequest request, @AuthenticationPrincipal Long memberId,
                                        @PathVariable Long articleId) {
        Long id = commentService.create(request, memberId, articleId);
        return ResponseEntity.status(200).body(id);
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<CommentTotalResponse> getComments(@PathVariable Long articleId) {
        CommentTotalResponse response = commentService.getComments(articleId);
        return ResponseEntity.status(200).body(response);
    }
}
