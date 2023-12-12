package com.dku.council.domain.post.repository.post;

import com.dku.council.domain.post.model.entity.posttype.Notice;

import java.util.List;

public interface NoticeRepository extends GenericPostRepository<Notice> {
    List<Notice> findTop5ByOrderByCreatedAtDesc();

}