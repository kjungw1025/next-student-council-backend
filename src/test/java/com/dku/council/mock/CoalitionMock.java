package com.dku.council.mock;

import com.dku.council.domain.post.model.CoalitionType;
import com.dku.council.domain.post.model.PostStatus;
import com.dku.council.domain.post.model.entity.Post;
import com.dku.council.domain.post.model.entity.posttype.Coalition;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.global.base.BaseEntity;
import com.dku.council.util.EntityUtil;
import com.dku.council.util.FieldReflector;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CoalitionMock {

    public static List<Coalition> createListDummy(String prefix, int size) {
        return createList(prefix, UserMock.createDummyMajor(), size, true, CoalitionType.FOOD);
    }

    public static List<Coalition> createList(String prefix, User user, int size, CoalitionType coalitionType) {
        return createList(prefix, user, size, true, coalitionType);
    }

    public static List<Coalition> createList(String prefix, User user, int size, boolean enabled, CoalitionType coalitionType) {
        List<Coalition> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Coalition coalition = Coalition.builder()
                    .user(user)
                    .title(prefix + i)
                    .body(Integer.toString(i))
                    .coalitionType(coalitionType)
                    .build();
            if (!enabled) {
                FieldReflector.inject(Post.class, coalition, "status", PostStatus.DELETED);
            }
            FieldReflector.inject(BaseEntity.class, coalition, "createdAt", LocalDateTime.of(2022, 3, 3, 3, 3));
            result.add(coalition);
        }

        return result;
    }

    public static Coalition createDummy() {
        return create(UserMock.createDummyMajor(), RandomGen.nextLong(), CoalitionType.FOOD);
    }

    public static Coalition createDummy(Long coalitionId) {
        return create(UserMock.createDummyMajor(), coalitionId, CoalitionType.FOOD);
    }

    public static Coalition create(User user) {
        return create(user, null, CoalitionType.FOOD);
    }

    public static Coalition create(User user, Long coalitionId, CoalitionType coalitionType) {
        Coalition coalition = Coalition.builder()
                .user(user)
                .title("")
                .body("")
                .coalitionType(coalitionType)
                .build();
        if (coalitionId != null) {
            EntityUtil.injectId(Post.class, coalition, coalitionId);
        }
        return coalition;
    }
}
