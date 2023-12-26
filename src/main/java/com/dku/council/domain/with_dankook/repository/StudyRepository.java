package com.dku.council.domain.with_dankook.repository;

import com.dku.council.domain.with_dankook.model.entity.type.Study;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyRepository extends WithDankookRepository<Study>{
    @Query("select s from Study s " +
            "join fetch s.tag " +
            "where s.tag.name = :name ")
    List<Study> findAllByStudyTagName(@Param("name") String name);
}
