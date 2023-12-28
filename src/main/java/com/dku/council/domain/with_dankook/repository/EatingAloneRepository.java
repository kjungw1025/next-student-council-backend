package com.dku.council.domain.with_dankook.repository;

import com.dku.council.domain.with_dankook.model.entity.type.EatingAlone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EatingAloneRepository extends WithDankookRepository<EatingAlone> {
    @Query("select e from EatingAlone e where e.masterUser.id = :userId and " +
            "(e.withDankookStatus='ACTIVE' or e.withDankookStatus='CLOSED') ")
    Page<EatingAlone> findAllEatingAloneByUserId(@Param("userId") Long userId, Pageable pageable);
}
