package com.dku.council.domain.studytag.repository;

import com.dku.council.domain.studytag.model.entity.StudyTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyTagRepository extends JpaRepository<StudyTag, Long> {
    @Query("select s from StudyTag s where s.name = :name")
    Optional<StudyTag> findByName(@Param("name") String name);

    @Query("select s from StudyTag s where s.id = :id")
    Optional<StudyTag> findById(@Param("id") Long id);

    @Query("select s from StudyTag s")
    List<StudyTag> findAll();
}
