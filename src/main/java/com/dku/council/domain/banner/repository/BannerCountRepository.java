package com.dku.council.domain.banner.repository;

import com.dku.council.domain.banner.model.entity.BannerCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerCountRepository extends JpaRepository<BannerCount, Long> {
}
