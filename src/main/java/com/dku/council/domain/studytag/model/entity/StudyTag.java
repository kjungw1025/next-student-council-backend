package com.dku.council.domain.studytag.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyTag {
    @Id
    @GeneratedValue
    @Column(name = "study_tag_id")
    private Long id;

    private String name;

    public StudyTag(String name) {
        this.name = name;
    }
}
