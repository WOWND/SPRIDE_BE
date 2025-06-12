package sprideapp.spride.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sprideapp.spride.article.Article;
import sprideapp.spride.article.ArticleRepository;
import sprideapp.spride.article.ArticleService;
import sprideapp.spride.article.dto.ArticleListResponse;
import sprideapp.spride.member.Member;
import sprideapp.spride.member.MemberService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final ArticleService articleService;
    private final CommentRepository commentRepository;
    private final MemberService memberService;

    public Long create(CommentRequest request, Long memberId,Long articleId) {
        Member member = memberService.findById(memberId);
        Article article = articleService.getArticle(articleId);
        Comment save = commentRepository.save(request.toEntity(member,article));
        return save.getId();
    }

    public CommentTotalResponse getComments(Long articleId) {
        Article article = articleService.getArticle(articleId);
        List<Comment> list = article.getComments();

        List<CommentResponse> list1 = list.stream()
                .map(CommentResponse::from)
                .toList();

        return new CommentTotalResponse(article.getMember().getNickname(), article.getContent(),
                article.getTime(), list1);

    }
}
