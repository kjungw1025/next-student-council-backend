package com.dku.council.domain.banner.repository;

import com.dku.council.domain.banner.model.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long> {
}
