package com.dku.council.domain.with_dankook.repository;

import com.dku.council.domain.with_dankook.model.entity.RoomMateSurvey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoomMateSurveyRepository extends JpaRepository<RoomMateSurvey, Long> {

    boolean existsByUserId(Long userId);

    @Query("select s from RoomMateSurvey s " +
            "where s.user.id = :userId")
    Optional<RoomMateSurvey> findByUserId(@Param("userId") Long userId);
}
