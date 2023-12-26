package com.dku.council.domain.with_dankook.repository;

import com.dku.council.domain.with_dankook.model.entity.type.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyRepository extends WithDankookRepository<Study>{

    @Query("select s from Study s where s.masterUser.id = :userId and " +
            "(s.withDankookStatus='ACTIVE' or s.withDankookStatus='CLOSED') ")
    Page<Study> findAllStudyByUserId(@Param("userId") Long userId, Pageable pageable);
}
