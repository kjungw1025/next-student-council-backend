package com.dku.council.domain.danfesta.repository;

import com.dku.council.domain.danfesta.model.entity.SpecialGuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpecialGuestRepository extends JpaRepository<SpecialGuest, Long> {

    @Query("select sg from SpecialGuest sg " +
            "join fetch sg.user u " +
            "join fetch u.major " +
            "where u.id=:userId")
    Optional<SpecialGuest> findByUserId(@Param("userId") Long userId);

    @Query("select sg from SpecialGuest sg " +
            "join fetch sg.user u " +
            "join fetch u.major " +
            "where u.studentId=:studentId")
    Optional<SpecialGuest> findByStudentId(@Param("studentId") String studentId);
}
