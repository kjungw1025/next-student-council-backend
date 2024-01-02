package com.dku.council.domain.with_dankook.repository;

import com.dku.council.domain.with_dankook.model.dto.list.SummarizedTradeDto;
import com.dku.council.domain.with_dankook.model.entity.WithDankook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WithDankookRepository<T extends WithDankook> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

    @Override
    @Query("select w from WithDankook w " +
            "join fetch w.masterUser u " +
            "join fetch u.major " +
            "where w.id=:id and w.withDankookStatus ='ACTIVE' ")
    Optional<T> findById(@Param("id") Long id);

    @Query("select w from WithDankook w " +
            "join fetch w.masterUser u " +
            "join fetch u.major " +
            "where w.id=:withDankookId and (w.withDankookStatus='CLOSED' or w.withDankookStatus='ACTIVE')")
    Optional<T> findWithClosedById(@Param("withDankookId") Long withDankookId);

    @Query("select w from WithDankook w " +
            "join fetch w.masterUser u " +
            "join fetch u.major " +
            "where w.id=:withDankookId and (w.withDankookStatus='CLOSED' or w.withDankookStatus='FULL' or w.withDankookStatus='ACTIVE')")
    Optional<T> findWithNotDeletedById(@Param("withDankookId") Long withDankookId);

    @Query("select w from WithDankook w " +
            "join fetch w.masterUser u " +
            "join fetch u.major " +
            "where w.id=:withDankookId ")
    Optional<T> findWithAllStatusById(@Param("withDankookId") Long withDankookId);

    @Query("select w from WithDankook w " +
            "where w.masterUser.id=:userId and (w.withDankookStatus='ACTIVE' or w.withDankookStatus='CLOSED')")
    Page<T> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("select w from WithDankook w " +
            "where w.withDankookStatus='ACTIVE' order by w.createdAt desc")
    Page<T> findTop5OrderByCreatedAtDesc(Pageable pageable);

    @Query(value = "select w.type from with_dankook w " +
                    "where w.with_dankook_id = :withDankookId ", nativeQuery = true)
    String findWithDankookType(@Param("withDankookId") Long withDankookId);

    @Query("select COUNT(*) from WithDankook w " +
            "where w.id = :withDankookId and " +
            "w.withDankookStatus = 'CLOSED'")
    int findWithClosedByIdToCreateReview(@Param("withDankookId") Long withDankookId);

    @Query("select COUNT(*) from WithDankook w " +
            "where w.id = :withDankookId and (w.withDankookStatus in ('FULL', 'CLOSED'))")
    int findWithClosedOrFullByIdToCreateReview(@Param("withDankookId") Long withDankookId);

    @Query(value = "select COUNT(*) from with_dankook w " +
            "where w.with_dankook_id = :withDankookId and " +
            "((w.with_dankook_status in ('FULL', 'CLOSED')) or (w.with_dankook_status = 'ACTIVE' and w.end_time <= CURRENT_TIMESTAMP())) ", nativeQuery = true)
    int findWithClosedOrFullOrActiveByIdToCreateReview(@Param("withDankookId") Long withDankookId);
}
