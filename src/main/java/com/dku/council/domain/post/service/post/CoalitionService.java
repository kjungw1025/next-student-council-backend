package com.dku.council.domain.post.service.post;

import com.dku.council.domain.post.exception.CoalitionTypeNotFoundException;
import com.dku.council.domain.post.model.CoalitionType;
import com.dku.council.domain.post.model.dto.list.SummarizedCoalitionDto;
import com.dku.council.domain.post.model.dto.request.RequestCreateCoalitionDto;
import com.dku.council.domain.post.model.dto.response.ResponseSingleGenericPostDto;
import com.dku.council.domain.post.model.entity.posttype.Coalition;
import com.dku.council.domain.post.repository.post.CoalitionRepository;
import com.dku.council.domain.post.repository.spec.PostSpec;
import com.dku.council.global.auth.role.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CoalitionService {

    private final GenericPostService<Coalition> postService;
    private final CoalitionRepository repository;

    @Transactional
    public Page<SummarizedCoalitionDto> list(String keyword, Pageable pageable, int bodySize, CoalitionType coalitionType) {
        Specification<Coalition> spec = PostSpec.withTitleOrBody(keyword);
        spec = spec.and(PostSpec.withCoalitionType(coalitionType));
        return postService.list(repository, spec, pageable, bodySize, SummarizedCoalitionDto::new);
    }

    public Long create(Long userId, RequestCreateCoalitionDto request) {
        if (request.getCoalitionType() == null){
            throw new CoalitionTypeNotFoundException();
        }
        return postService.create(repository, userId, request);
    }

    public ResponseSingleGenericPostDto findOne(Long id, Long userId, UserRole role, String address) {
        return postService.findOne(repository, id, userId, role, address);
    }

    public void delete(Long id, Long userId, boolean admin) {
        postService.delete(repository, id, userId, admin);
    }
}
