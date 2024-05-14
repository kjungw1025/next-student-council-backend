package com.dku.council.domain.danfesta.model.entity;

import com.dku.council.global.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LineUpImage extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "line_up_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_up_id")
    private LineUp lineUp;

    private String fileId;

    private String mimeType;

    private String fileName;

    @Lob
    private String blurData;

    @Builder
    private LineUpImage(String fileId, String mimeType, String fileName, String blurData) {
        this.fileId = fileId;
        this.mimeType = mimeType;
        this.fileName = fileName;
        this.blurData = blurData;
    }

    public void changeLineUp(LineUp lineUp) {
        if (this.lineUp != null) {
            this.lineUp.getImages().remove(this);
        }

        this.lineUp = lineUp;
        this.lineUp.getImages().add(this);
    }
}
