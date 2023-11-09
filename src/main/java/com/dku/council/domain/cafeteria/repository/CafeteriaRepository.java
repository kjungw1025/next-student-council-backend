package com.dku.council.domain.cafeteria.repository;

import com.dku.council.domain.cafeteria.model.entity.Cafeteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface CafeteriaRepository extends JpaRepository<Cafeteria, Long> {

//    @Query("select new com.dku.council.domain.cafeteria.model.dto.response.ResponseCafeteriaDto(" +
//            "c.mealDate, c.breakfast, c.lunch, c.dinner, c.other" +
//            ") " +
//            "from Cafeteria c where c.mealDate = :mealDate")
//    List<ResponseCafeteriaDto> findByMealDate(@Param("mealDate") LocalDate mealDate);

    @Query("select c from Cafeteria c where c.mealDate = :mealDate")
    Optional<Cafeteria> findByMealDate(@Param("mealDate") LocalDate mealDate);
}
