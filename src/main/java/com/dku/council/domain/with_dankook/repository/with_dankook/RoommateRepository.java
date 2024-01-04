package com.dku.council.domain.with_dankook.repository.with_dankook;

import com.dku.council.domain.with_dankook.model.entity.type.Roommate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface RoommateRepository extends WithDankookRepository<Roommate>{
    @Query("select r from Roommate r " +
            "where r.masterUser.gender = :gender " +
            "and r.withDankookStatus = 'ACTIVE' " +
            "order by r.createdAt desc")
    Page<Roommate> findAllByUserGender(@Param("gender") String gender, Pageable pageable);

    @Query("select r from Roommate r " +
            "where r.masterUser.id = :userId and " +
            "(r.withDankookStatus != 'DELETED' or r.withDankookStatus != 'DELETED_BY_ADMIN') ")
    Optional<Roommate> findByUserId(@Param("userId") Long userId);
}
