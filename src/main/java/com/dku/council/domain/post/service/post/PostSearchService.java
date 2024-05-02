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
    public SummarizedPostSearchDto searchPost(String keyword, Pageable pageable) {
        Page<Notice> notices = getNoticeLists(keyword, pageable);
        Page<Coalition> coalitions = getCoalitionLists(keyword, pageable);
        Page<Petition> petitions = getPetitionLists(keyword, pageable);
        Page<Conference> conferences = getConferencesLists(keyword, pageable);
        Page<Rule> rules = getRulesLists(keyword, pageable);
        return new SummarizedPostSearchDto(
                new ResponsePage<>(notices.map(ResponseSingleSearchPost::new)),
                new ResponsePage<>(coalitions.map(ResponseSingleSearchPost::new)),
                new ResponsePage<>(petitions.map(ResponseSingleSearchPost::new)),
                new ResponsePage<>(conferences.map(ResponseSingleSearchPost::new)),
                new ResponsePage<>(rules.map(ResponseSingleSearchPost::new))
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

    private Page<Conference> getConferencesLists(String keyword, Pageable pageable) {
        Specification<Conference> spec = PostSpec.withTitleOrBody(keyword);
        spec = spec.and(PostSpec.withActive());
        return conferenceRepository.findAll(spec,pageable);
    }

    private Page<Rule> getRulesLists(String keyword, Pageable pageable) {
        Specification<Rule> spec = PostSpec.withTitleOrBody(keyword);
        spec = spec.and(PostSpec.withActive());
        return ruleRepository.findAll(spec,pageable);
    }
}
