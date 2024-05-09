package com.dku.council.domain.post.service.post;

import com.dku.council.domain.post.model.dto.response.ResponsePage;
import com.dku.council.domain.post.model.dto.response.ResponseSingleSearchPost;
import com.dku.council.domain.post.model.entity.posttype.*;
import com.dku.council.domain.post.repository.post.*;
import com.dku.council.domain.post.repository.spec.PostSpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostSearchService {

    private final PetitionRepository petitionRepository;
    private final NoticeRepository noticeRepository;
    private final CoalitionRepository coalitionRepository;
    private final ConferenceRepository conferenceRepository;
    private final RuleRepository ruleRepository;

    /**
     * 모든 게시글 검색
     */
    public SummarizedPostSearchDto searchPost(String keyword, Pageable pageable, int bodySize) {
        Page<Notice> notices = getNoticeLists(keyword, pageable);
        Page<Coalition> coalitions = getCoalitionLists(keyword, pageable);
        Page<Petition> petitions = getPetitionLists(keyword, pageable);
        return new SummarizedPostSearchDto(
                new ResponsePage<>(notices.map(notice -> new ResponseSingleSearchPost(notice, bodySize))),
                new ResponsePage<>(coalitions.map(coalition -> new ResponseSingleSearchPost(coalition, bodySize))),
                new ResponsePage<>(petitions.map(petition -> new ResponseSingleSearchPost(petition, bodySize)))
        );
    }

    private Page<Notice> getNoticeLists(String keyword, Pageable pageable) {
        Specification<Notice> spec = PostSpec.withTitleOrBody(keyword);
        spec = spec.and(PostSpec.withActive());
        return noticeRepository.findAll(spec, pageable);
    }

    private Page<Coalition> getCoalitionLists(String keyword, Pageable pageable) {
        Specification<Coalition> spec = PostSpec.withTitleOrBody(keyword);
        spec = spec.and(PostSpec.withActive());
        return coalitionRepository.findAll(spec,pageable);
    }

    private Page<Petition> getPetitionLists(String keyword, Pageable pageable) {
        Specification<Petition> spec = PostSpec.withTitleOrBody(keyword);
        spec = spec.and(PostSpec.withActive());
        return petitionRepository.findAll(spec,pageable);
    }
}
