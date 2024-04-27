package com.dku.council.domain.banner.repository.impl;

import com.dku.council.domain.banner.repository.BannerResetMemoryRepository;
import com.dku.council.global.config.redis.RedisKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.dku.council.global.config.redis.RedisKeys.combine;

@Repository
@RequiredArgsConstructor
public class BannerResetRedisRepository implements BannerResetMemoryRepository {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void clearAllDistinct(Long bannerId) {
        String rootKey = RedisKeys.BANNER_KEY;
        Object[] distinctObjs = redisTemplate.opsForHash().keys(rootKey).stream()
                .filter(key -> key.toString().startsWith(combine(RedisKeys.BANNER_DISTINCT_KEY, bannerId.toString())))
                .toArray();
        redisTemplate.opsForHash().delete(rootKey, distinctObjs);
    }
}
