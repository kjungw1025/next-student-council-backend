package com.dku.council.domain.with_dankook.model.entity.type;

import com.dku.council.domain.studytag.model.entity.StudyTag;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.with_dankook.model.entity.WithDankook;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:MM")
    private LocalDateTime startTime;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:MM")
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_tag_id")
    private StudyTag tag;

    @NotNull
    @Lob
    private String content;

    @Builder
    private Study(User user, String title, int minStudentId, LocalDateTime startTime, LocalDateTime endTime, StudyTag tag, String content) {
        super(user);
        this.title = title;
        this.minStudentId = minStudentId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tag = tag;
        this.content = content;
    }
    @Override
    public String getDisplayingUsername() {
        return getMasterUser().getNickname();
    }
}
