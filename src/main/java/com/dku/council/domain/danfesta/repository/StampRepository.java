package com.dku.council.domain.danfesta.repository;

import com.dku.council.domain.danfesta.model.entity.Stamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StampRepository extends JpaRepository<Stamp, Long>, JpaSpecificationExecutor<Stamp> {

    @Query("select s from Stamp s " +
            "join fetch s.user u " +
            "join fetch u.major " +
            "where u.id=:userId")
    Optional<Stamp> findByUserId(@Param("userId") Long userId);

    @Query("select s from Stamp s " +
            "join fetch s.user u " +
            "join fetch u.major " +
            "where u.studentId=:studentId")
    Optional<Stamp> findByStudentId(@Param("studentId") String studentId);
}
