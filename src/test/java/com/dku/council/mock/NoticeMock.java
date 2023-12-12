package com.dku.council.mock;

import com.dku.council.domain.post.model.PostStatus;
import com.dku.council.domain.post.model.entity.Post;
import com.dku.council.domain.post.model.entity.posttype.Notice;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.global.base.BaseEntity;
import com.dku.council.util.EntityUtil;
import com.dku.council.util.FieldReflector;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NoticeMock {
    public static List<Notice> createListDummy(String prefix, int size) {
        return createList(prefix, UserMock.createDummyMajor(), size, true);
    }

    public static List<Notice> createList(String prefix, User user, int size) {
        return createList(prefix, user, size, true);
    }

    public static List<Notice> createList(String prefix, User user, int size, boolean enabled) {
        List<Notice> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Notice notice = Notice.builder()
                    .user(user)
                    .title(prefix + i)
                    .body(Integer.toString(i))
                    .build();
            if (!enabled) {
                FieldReflector.inject(Post.class, notice, "status", PostStatus.DELETED);
            }
            FieldReflector.inject(BaseEntity.class, notice, "createdAt", LocalDateTime.of(2022, 3, 3, 3, 3));
            result.add(notice);
        }

        return result;
    }

    public static Notice createDummy() {
        return create(UserMock.createDummyMajor(), RandomGen.nextLong());
    }

    public static Notice createDummy(Long noticeId) {
        return create(UserMock.createDummyMajor(), noticeId);
    }

    public static Notice create(User user) {
        return create(user, null);
    }

    public static Notice create(User user, Long noticeId) {
        Notice notice = Notice.builder()
                .user(user)
                .title("")
                .body("")
                .build();
        if (noticeId != null) {
            EntityUtil.injectId(Post.class, notice, noticeId);
        }
        return notice;
    }
}
