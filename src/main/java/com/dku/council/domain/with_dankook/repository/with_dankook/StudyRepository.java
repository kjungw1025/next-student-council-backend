package com.dku.council.domain.with_dankook.repository.with_dankook;

import com.dku.council.domain.with_dankook.model.entity.type.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyRepository extends WithDankookRepository<Study>{

    @Query("select s from Study s where s.masterUser.id = :userId and " +
            "(s.withDankookStatus='ACTIVE' or s.withDankookStatus='CLOSED') ")
    Page<Study> findAllStudyByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("select s from Study s " +
            "join WithDankookUser u " +
            "on s.id = u.withDankook.id " +
            "where u.participant.id = :userId and u.reviewStatus = false and " +
            "((s.withDankookStatus in ('FULL', 'CLOSED')) or (s.withDankookStatus = 'ACTIVE' and s.endTime <= CURRENT_TIMESTAMP)) " +
            "order by s.lastModifiedAt DESC ")
    Page<Study> findAllPossibleReviewPost(@Param("userId") Long userId,
                                          Pageable pageable);
}
