package com.dku.council.domain.with_dankook.model.entity.type;

import com.dku.council.domain.tag.model.entity.StudyTag;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.with_dankook.model.entity.WithDankook;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.*;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Study extends WithDankook {

    @Id
    @GeneratedValue
    @Column(name = "study_id")
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private int minStudentId;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @OneToMany
    private List<StudyTag> tags = new ArrayList<>();

    @NotNull
    @Lob
    private String content;

    @Builder
    private Study(@NonNull User user, @NonNull String chatLink, @NonNull String title, @NonNull int minStudentId, @NonNull LocalDateTime startTime, @NonNull LocalDateTime endTime, @NonNull String content) {
        super(user, chatLink);
        this.title = title;
        this.minStudentId = minStudentId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.content = content;
    }
    @Override
    public String getDisplayingUsername() {
        return getMasterUser().getNickname();
    }
}
