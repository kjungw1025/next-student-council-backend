package com.dku.council.domain.with_dankook.repository.impl;

import com.dku.council.domain.with_dankook.repository.WithDankookMemoryRepository;
import com.dku.council.global.base.AbstractKeyValueCacheRepository;
import com.dku.council.global.config.redis.RedisKeys;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;

@Repository
public class WithDankookRedisRepository extends AbstractKeyValueCacheRepository implements WithDankookMemoryRepository {

    public WithDankookRedisRepository(StringRedisTemplate redisTemplate,
                                      ObjectMapper objectMapper) {
        super(redisTemplate, objectMapper, RedisKeys.WITH_DANKOOK_WRTIE_KEY);
    }

    @Override
    public boolean isAlreadyContains(String withDankookType, Long userId, Instant now) {
        String key = RedisKeys.combine(RedisKeys.WITH_DANKOOK_WRTIE_KEY, withDankookType, userId);
        return get(key, String.class, now).isPresent();
    }

    @Override
    public void put(String withDankookType, Long userId, Duration expiresAfter, Instant now) {
        String key = RedisKeys.combine(RedisKeys.WITH_DANKOOK_WRTIE_KEY, withDankookType, userId);
        set(key, "", now, expiresAfter);
    }
}
