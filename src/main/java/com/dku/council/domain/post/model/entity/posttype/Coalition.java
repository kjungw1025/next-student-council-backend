package com.dku.council.domain.post.model.entity.posttype;

import com.dku.council.domain.post.model.CoalitionType;
import com.dku.council.domain.post.model.entity.Post;
import com.dku.council.domain.user.model.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import static javax.persistence.EnumType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Coalition extends Post {

    @Enumerated(STRING)
    @NotNull
    private CoalitionType coalitionType;

    @Builder
    private Coalition (@NonNull User user,
                       @NonNull String title,
                       @NonNull String body,
                       int views,
                       @NotNull CoalitionType coalitionType) {
        super(user, title, body, views);
        this.coalitionType = coalitionType;
    }

    @Override
    @Transient
    public String getDisplayingUsername() {
        return getUser().getNickname();
    }
}
