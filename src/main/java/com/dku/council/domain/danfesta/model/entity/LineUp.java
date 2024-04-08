package com.dku.council.domain.danfesta.model.entity;

import com.dku.council.domain.danfesta.model.FestivalDate;
import com.dku.council.global.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "line_up")
public class LineUp extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String singer;

    @OneToMany(mappedBy = "lineUp", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineUpImage> images = new ArrayList<>();

    @Lob
    private String description;

    private LocalDateTime performanceTime;

    @Enumerated(EnumType.STRING)
    private FestivalDate festivalDate;

    private boolean isOpened;

    @Builder
    private LineUp(String singer, String description, LocalDateTime performanceTime, FestivalDate festivalDate, boolean isOpened) {
        this.singer = singer;
        this.description = description;
        this.performanceTime = performanceTime;
        this.festivalDate = festivalDate;
        this.isOpened = isOpened;
    }

    public void setDefaultImages() {
        this.images = new ArrayList<>();
    }

    public void changeIsOpened() {
        this.isOpened = !this.isOpened;
    }
}
