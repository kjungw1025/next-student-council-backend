package com.dku.council.domain.danfesta.repository;

import com.dku.council.domain.danfesta.model.FestivalDate;
import com.dku.council.domain.danfesta.model.entity.LineUp;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LineUpRepository extends JpaRepository<LineUp, Long> {

    List<LineUp> findAll(Specification<LineUp> spec);

    @Query("select l from LineUp  l where l.festivalDate =:festivalDate")
    List<LineUp> findAllByFestivalDate(@Param("festivalDate") FestivalDate festivalDate);
}
