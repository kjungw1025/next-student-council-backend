package com.dku.council.domain.danfesta.repository;

import com.dku.council.domain.danfesta.model.entity.LineUp;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LineUpRepository extends JpaRepository<LineUp, Long> {

    List<LineUp> findAll(Specification<LineUp> spec);
}
