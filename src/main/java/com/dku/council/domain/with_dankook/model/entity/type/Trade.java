package com.dku.council.domain.with_dankook.model.entity.type;

import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.with_dankook.model.entity.TradeImage;
import com.dku.council.domain.with_dankook.model.entity.WithDankook;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Trade extends WithDankook {

    @NotNull
    private String title;

    @NotNull
    private int price;

    @NotNull
    private String content;

    @NotNull
    private String tradePlace;

    @OneToMany(mappedBy = "trade", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<TradeImage> images = new ArrayList<>();

    @Builder
    private Trade(User user, String title, int price, String content, String tradePlace) {
        super(user);
        this.title = title;
        this.price = price;
        this.content = content;
        this.tradePlace = tradePlace;
    }

    @Override
    @Transient
    public String getDisplayingUsername() {
        return getMasterUser().getNickname();
    }
}
