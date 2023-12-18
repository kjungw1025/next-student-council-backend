package com.dku.council.domain.with_dankook.repository;

import com.dku.council.domain.with_dankook.model.entity.WithDankook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WithDankookRepository<T extends WithDankook> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

    @Override
    @Query("select w from WithDankook w " +
            "join fetch w.masterUser u " +
            "join fetch u.major " +
            "where w.id=:id and w.withDankookStatus ='ACTIVE' ")
    Optional<T> findById(@Param("id") Long id);
}
