package com.dku.council.domain.with_dankook.model.entity;

import com.dku.council.domain.with_dankook.model.entity.type.Trade;
import com.dku.council.global.base.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class TradeImage extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "trade_image_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "trade_id")
    private Trade trade;

    private String fileId;

    private String thumbnailId;

    private String mimeType;

    private String fileName;

    @Builder
    private TradeImage(String fileId, String thumbnailId, String fileName, String mimeType) {
        this.fileId = fileId;
        this.thumbnailId = thumbnailId;
        this.fileName = fileName;
        this.mimeType = mimeType;
    }

    public void changeTrade(Trade trade) {
        if (this.trade != null) {
            this.trade.getImages().remove(this);
        }

        this.trade = trade;
        this.trade.getImages().add(this);
    }
}
