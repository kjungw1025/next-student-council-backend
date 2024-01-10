package com.dku.council.domain.with_dankook.repository.with_dankook;

import com.dku.council.domain.with_dankook.model.entity.type.BearEats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BearEatsRepository extends WithDankookRepository<BearEats>{

    @Query("select b from BearEats b where b.masterUser.id = :userId and " +
            "(b.withDankookStatus='ACTIVE' or b.withDankookStatus='CLOSED') ")
    Page<BearEats> findAllBearEatsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("select b from BearEats b " +
            "join WithDankookUser u " +
            "on b.id = u.withDankook.id " +
            "where u.participant.id = :userId and u.reviewStatus = false and " +
            "((b.withDankookStatus in ('FULL', 'CLOSED')) or (b.withDankookStatus = 'ACTIVE' and b.deliveryTime <= CURRENT_TIMESTAMP)) " +
            "order by b.lastModifiedAt DESC ")
    Page<BearEats> findAllPossibleReviewPost(@Param("userId") Long userId,
                                          Pageable pageable);
}
