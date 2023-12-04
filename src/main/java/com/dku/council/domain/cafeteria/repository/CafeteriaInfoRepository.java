package com.dku.council.domain.cafeteria.repository;

import com.dku.council.domain.cafeteria.model.dto.response.ResponseCafeteriaInfoDto;
import com.dku.council.domain.cafeteria.model.entity.CafeteriaInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CafeteriaInfoRepository extends JpaRepository<CafeteriaInfo, Long> {

    @Query("select c from CafeteriaInfo c where c.mealDate = :mealDate")
    Optional<CafeteriaInfo> findByMealDate(@Param("mealDate") LocalDate mealDate);
}
