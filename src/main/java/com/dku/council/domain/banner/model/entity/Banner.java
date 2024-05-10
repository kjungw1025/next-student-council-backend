package com.dku.council.domain.banner.model.entity;

import com.dku.council.domain.banner.model.BannerImage;
import com.dku.council.global.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Banner extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "banner_id")
    private Long id;

    private String bannerUrl;

    @OneToMany(mappedBy = "banner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BannerImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "banner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BannerCount> counts = new ArrayList<>();

    private int viewCount;

    private int distinctViewCount;

    private int clickCount;

    @Builder
    private Banner(String bannerUrl) {
        this.bannerUrl = bannerUrl;
        this.viewCount = 0;
        this.distinctViewCount = 0;
        this.clickCount = 0;
    }

    public void addViewDistinctCount() {
        this.distinctViewCount++;
    }

    public void addViewCount() {
        this.viewCount++;
    }

    public void addClickCount() {
        this.clickCount++;
    }

    /**
     * 배너의 중복 조회 수를 매일 00시에 초기화합니다.
     */
    public void resetDistinctViewCount() {
        this.distinctViewCount = 0;
    }
}
