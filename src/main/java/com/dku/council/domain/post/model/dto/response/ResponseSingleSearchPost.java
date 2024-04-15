package com.dku.council.domain.post.model.dto.response;

import com.dku.council.domain.post.model.entity.Post;
import lombok.Getter;

@Getter
public class ResponseSingleSearchPost {

    private final Long id;

    private final String title;

    public ResponseSingleSearchPost(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
    }
}
