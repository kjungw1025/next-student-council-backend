package com.dku.council.domain.danfesta.model.entity;

import com.dku.council.domain.user.model.entity.User;
import com.dku.council.global.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "stamp")
public class Stamp extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "stamp_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private boolean mission1;

    private boolean mission2;

    private boolean mission3;

    private boolean mission4;

    private boolean mission5;

    private boolean mission6;

    private boolean mission7;

    @Builder
    private Stamp(User user) {
        this.user = user;
        this.mission1 = false;
        this.mission2 = false;
        this.mission3 = false;
        this.mission4 = false;
        this.mission5 = false;
        this.mission6 = false;
        this.mission7 = false;
    }

    public void stampToMission(String booth) {
        switch (booth) {
            case "mission1":
                this.mission1 = !this.mission1;
                break;
            case "mission2":
                this.mission2 = !this.mission2;
                break;
            case "mission3":
                this.mission3 = !this.mission3;
                break;
            case "mission4":
                this.mission4 = !this.mission4;
                break;
            case "mission5":
                this.mission5 = !this.mission5;
                break;
            case "mission6":
                this.mission6 = !this.mission6;
                break;
            case "mission7":
                this.mission7 = !this.mission7;
                break;
        }
    }
}
