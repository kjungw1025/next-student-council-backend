package com.dku.council.domain.with_dankook.repository;

import com.dku.council.domain.with_dankook.model.entity.WithDankookUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WithDankookUserRepository extends JpaRepository<WithDankookUser, Long>, JpaSpecificationExecutor<WithDankookUser> {
    @Query("select COUNT(u.withDankook.id) " +
            "from WithDankookUser u " +
            "where u.withDankook.withDankookStatus = 'ACTIVE' and " +
                    "u.participantStatus = 'VALID' " +
            "group by u.withDankook.id " +
            "having u.withDankook.id = :id ")
    int findRecruitedById(@Param("id") Long id);
}