package com.dku.council.domain.banner.repository;

import com.dku.council.domain.banner.model.BannerImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BannerImageRepository extends JpaRepository<BannerImage, Long> {

    @Query("select bi from BannerImage bi where bi.banner.id =:id")
    Optional<BannerImage> findByBannerId(@Param("id") Long id);
}
