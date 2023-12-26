package com.dku.council.domain.with_dankook.model.entity.type;

import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.with_dankook.model.entity.WithDankook;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import static lombok.AccessLevel.*;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Dormitory extends WithDankook {

    @Id
    @GeneratedValue
    @Column(name = "dormitory_id")
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private int minStudentId;

    @NotNull
    private String livingHall;

    @NotNull
    private String duration;

    @Builder
    private Dormitory(User user, String title, int minStudentId, String livingHall, String duration) {
        super(user);
        this.title = title;
        this.minStudentId = minStudentId;
        this.livingHall = livingHall;
        this.duration = duration;
    }

    @Override
    public String getDisplayingUsername() {
        return getMasterUser().getNickname();
    }
}
