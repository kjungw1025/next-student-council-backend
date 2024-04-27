package com.dku.council.domain.banner.repository.impl;

import com.dku.council.domain.banner.repository.BannerMemoryRepository;
import com.dku.council.global.base.AbstractKeyValueCacheRepository;
import com.dku.council.global.config.redis.RedisKeys;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;

import static com.dku.council.global.config.redis.RedisKeys.combine;

@Repository
public class BannerRedisRepository extends AbstractKeyValueCacheRepository implements BannerMemoryRepository {

    public BannerRedisRepository(StringRedisTemplate redisTemplate,
                                 ObjectMapper objectMapper) {
        super(redisTemplate, objectMapper, RedisKeys.BANNER_KEY);
    }

    @Override
    public void putDistinct(Long bannerId, String remoteAddress, Duration expiresAfter, Instant now) {
        String key = combine(RedisKeys.BANNER_DISTINCT_KEY, bannerId, remoteAddress);
        set(key, "", now, expiresAfter);
    }

    @Override
    public void putCount(Long bannerId, String remoteAddress, Duration expiresAfter, Instant now) {
        String key = combine(RedisKeys.BANNER_COUNT_KEY, bannerId, remoteAddress);
        set(key, "", now, expiresAfter);
    }

    @Override
    public void putClick(Long bannerId, String remoteAddress, Duration expiresAfter, Instant now) {
        String key = combine(RedisKeys.BANNER_CLICK_KEY, bannerId, remoteAddress);
        set(key, "", now, expiresAfter);
    }

    @Override
    public boolean existDistinct(Long bannerId, String remoteAddress, Instant now) {
        String key = combine(RedisKeys.BANNER_DISTINCT_KEY, bannerId, remoteAddress);
        return get(key, String.class, now).isPresent();
    }

    @Override
    public boolean existCount(Long bannerId, String remoteAddress, Instant now) {
        String key = combine(RedisKeys.BANNER_COUNT_KEY, bannerId, remoteAddress);
        return get(key, String.class, now).isPresent();
    }

    @Override
    public boolean existClick(Long bannerId, String remoteAddress, Instant now) {
        String key = combine(RedisKeys.BANNER_CLICK_KEY, bannerId, remoteAddress);
        return get(key, String.class, now).isPresent();
    }
}
