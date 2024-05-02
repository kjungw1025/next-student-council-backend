package com.dku.council.domain.post.model.dto.response;

import com.dku.council.domain.post.model.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResponseSingleSearchPost {

    private final Long id;

    private final String title;

    private final String body;

    private final LocalDateTime createdAt;

    public ResponseSingleSearchPost(Post post) {
        this.id = post.getId();
        this.title = sliceTitle(post.getTitle());
        this.body = sliceBody(post.getBody());
        this.createdAt = post.getCreatedAt();
    }

    private String sliceTitle(String title) {
        if (title.length() > 50) {
            return title.substring(0, 50);
        }
        return title;
    }

    private String sliceBody(String body) {
        if (body.length() > 100) {
            return body.substring(0, 100);
        }
        return body;
    }
}
