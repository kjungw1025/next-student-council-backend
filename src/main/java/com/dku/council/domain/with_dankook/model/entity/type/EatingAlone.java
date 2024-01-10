package com.dku.council.domain.with_dankook.model.entity.type;

import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.with_dankook.model.entity.WithDankook;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class EatingAlone extends WithDankook {

    @Id
    @GeneratedValue
    @Column(name = "eating_along_id")
    private Long id;

    @NotNull
    private String title;

    @NotNull
    @Lob
    private String content;

    @Builder
    private EatingAlone(User user, String title, String content) {
        super(user);
        this.title = title;
        this.content = content;
    }

    @Override
    public String getDisplayingUsername() {
        return getMasterUser().getNickname();
    }
}
