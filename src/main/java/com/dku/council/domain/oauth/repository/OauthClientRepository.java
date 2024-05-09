package com.dku.council.domain.oauth.repository;

import com.dku.council.domain.oauth.model.entity.OauthClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OauthClientRepository extends JpaRepository<OauthClient, Long> {
    Optional<OauthClient> findByClientId(String clientId);
}
