package com.dku.council.domain.with_dankook.model.entity;

import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.with_dankook.model.WithDankookStatus;
import com.dku.council.global.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static com.dku.council.domain.with_dankook.model.WithDankookStatus.ACTIVE;
import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.InheritanceType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@DynamicUpdate
@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@NoArgsConstructor(access = PROTECTED)
public abstract class WithDankook extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "with_dankook_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "master_user_id")
    private User masterUser;

    @OneToMany(mappedBy = "withDankook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WithDankookUser> users = new ArrayList<>();

    @NonNull
    @Lob
    @Column(name = "chat_link")
    private String chatLink;

    @Enumerated(STRING)
    private WithDankookStatus withDankookStatus;

    protected WithDankook(User user, String chatLink) {
        this.masterUser = user;
        this.chatLink = chatLink;
        this.withDankookStatus = ACTIVE;
    }

    public abstract String getDisplayingUsername();

    public void markAsDeleted(boolean byAdmin) {
        this.withDankookStatus = byAdmin ? WithDankookStatus.DELETED_BY_ADMIN : WithDankookStatus.DELETED;
    }

    public boolean isClosed() {
        return withDankookStatus == WithDankookStatus.CLOSED;
    }

    public void close() {
        this.withDankookStatus = WithDankookStatus.CLOSED;
    }
}
