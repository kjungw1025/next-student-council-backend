package com.dku.council.domain.with_dankook.model.entity.type;

import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.with_dankook.model.entity.WithDankook;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class BearEats extends WithDankook {

    @Id
    @GeneratedValue
    @Column(name = "bear_eats_id")
    private Long id;

    @NotNull
    private String restaurant;

    @NotNull
    private String deliveryPlace;

    @NotNull
    private LocalDateTime deliveryTime;

    @NotNull
    @Lob
    private String content;


    @Builder
    private BearEats(User user, String chatLink, String restaurant, String deliveryPlace, LocalDateTime deliveryTime, String content) {
        super(user, chatLink);
        this.restaurant = restaurant;
        this.deliveryPlace = deliveryPlace;
        this.deliveryTime = deliveryTime;
        this.content = content;
    }
    @Override
    public String getDisplayingUsername() {
        return getMasterUser().getNickname();
    }
}
