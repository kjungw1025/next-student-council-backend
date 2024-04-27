package com.dku.council.domain.banner.model.entity;

import com.dku.council.global.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BannerCount extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "banner_count_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "banner_id")
    private Banner banner;

    private LocalDateTime dailyDistinctViewDate;

    private int distinctViewCount;

    @Builder
    private BannerCount(Banner banner, LocalDateTime dailyDistinctViewDate, int distinctViewCount) {
        this.banner = banner;
        this.dailyDistinctViewDate = dailyDistinctViewDate;
        this.distinctViewCount = distinctViewCount;
    }
}
