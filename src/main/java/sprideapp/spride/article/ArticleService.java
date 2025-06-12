package sprideapp.spride.article;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sprideapp.spride.article.dto.ArticleListResponse;
import sprideapp.spride.article.dto.CreateArticleRequest;
import sprideapp.spride.member.Member;
import sprideapp.spride.member.MemberService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberService memberService;

    public Long create(CreateArticleRequest request, Long memberId) {
        Member member = memberService.findById(memberId);
        Article save = articleRepository.save(request.toEntity(member));
        return save.getId();
    }

    @Transactional(readOnly = true)
    public List<ArticleListResponse> getArticles(ArticleType type) {
        List<Article> list = articleRepository.findByType(type);
        if (type == ArticleType.TAXI) {
            return list.stream()
                    .map(ArticleListResponse::fromTaxi)
                    .toList();
        } else {
            return list.stream()
                    .map(ArticleListResponse::fromLost)
                    .toList();
        }
    }
    @Transactional(readOnly = true)
    public Article getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다: id=" + articleId));
    }
}
