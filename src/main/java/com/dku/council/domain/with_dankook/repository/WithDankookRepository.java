package com.dku.council.domain.with_dankook.repository;

import com.dku.council.domain.with_dankook.model.entity.WithDankook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WithDankookRepository<T extends WithDankook> extends JpaRepository<WithDankook, Long>, JpaSpecificationExecutor<T> {
}
