package sprideapp.spride.article;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sprideapp.spride.article.dto.ArticleListResponse;
import sprideapp.spride.article.dto.CreateArticleRequest;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleApiController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody CreateArticleRequest request, @AuthenticationPrincipal Long memberId) {
        Long id = articleService.create(request, memberId);
        return ResponseEntity.status(200).body(id);
    }


    @GetMapping
    public ResponseEntity<List<ArticleListResponse>> get(@RequestParam("type") ArticleType type) {
        List<ArticleListResponse> articles = articleService.getArticles(type);
        return ResponseEntity.status(200).body(articles);
    }



}
