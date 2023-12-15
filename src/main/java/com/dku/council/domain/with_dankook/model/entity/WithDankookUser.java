package com.dku.council.domain.with_dankook.model.entity;

import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.with_dankook.model.ParticipantStatus;
import com.dku.council.global.base.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.dku.council.domain.with_dankook.model.ParticipantStatus.VALID;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class WithDankookUser extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "with_dankook_user_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "participant_id")
    private User participant;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "with_dankook_id")
    private WithDankook withDankook;

    private ParticipantStatus participantStatus;

    @Builder
    public WithDankookUser(User user, WithDankook withDankook) {
        this.participant = user;
        this.withDankook = withDankook;
        this.participantStatus = VALID;
    }
}
