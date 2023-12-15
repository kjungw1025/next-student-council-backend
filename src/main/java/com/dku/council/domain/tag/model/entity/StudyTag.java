package com.dku.council.domain.tag.model.entity;

import com.dku.council.domain.with_dankook.model.entity.type.Study;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class StudyTag {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public StudyTag(Tag tag) {
        this.tag = tag;
    }

    public void changeStudy(Study study) {
        if (this.study != null) {
            this.study.getTags().remove(this);
        }

        this.study = study;
        this.study.getTags().add(this);
    }
}
