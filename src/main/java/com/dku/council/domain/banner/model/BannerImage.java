package com.dku.council.domain.banner.model;

import com.dku.council.domain.banner.model.entity.Banner;
import com.dku.council.global.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BannerImage extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "banner_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_id")
    private Banner banner;

    private String fileId;


    private String mimeType;

    private String fileName;

    @Builder
    private BannerImage(String fileId, String mimeType, String fileName) {
        this.fileId = fileId;
        this.mimeType = mimeType;
        this.fileName = fileName;
    }

    public void changeBanner(Banner banner) {
        if (this.banner != null) {
            this.banner.getImages().remove(this);
        }

        this.banner = banner;
        this.banner.getImages().add(this);
    }
}
