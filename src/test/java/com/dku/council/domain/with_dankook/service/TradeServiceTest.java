package com.dku.council.domain.with_dankook.service;

import com.dku.council.domain.like.service.LikeService;
import com.dku.council.domain.post.repository.PostTimeMemoryRepository;
import com.dku.council.domain.post.service.ThumbnailService;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.domain.with_dankook.model.entity.type.Trade;
import com.dku.council.domain.with_dankook.repository.TradeRepository;
import com.dku.council.domain.with_dankook.repository.WithDankookMemoryRepository;
import com.dku.council.infra.nhn.s3.service.ImageUploadService;
import com.dku.council.infra.nhn.s3.service.ObjectUploadContext;
import com.dku.council.util.ClockUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TradeServiceTest {

    private final Clock clock = ClockUtil.create();

    private final Duration writeCooltime = Duration.ofDays(1);

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private WithDankookMemoryRepository withDankookMemoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WithDankookService<Trade> withDankookService;

    @Mock
    private LikeService likeService;

    @Mock
    private ImageUploadService imageUploadService;

    @Mock
    private ThumbnailService thumbnailService;

    @Mock
    private ObjectUploadContext objectUploadContext;



    private TradeService tradeService;

    @BeforeEach
    public void setup() {
        tradeService = new TradeService(tradeRepository, withDankookMemoryRepository, userRepository,
                withDankookService, likeService, imageUploadService, thumbnailService, objectUploadContext,
                clock, writeCooltime);
    }

    @Test
    @DisplayName("글 작성 - 쿨타임 이전에 작성한 적 없는 경우")
    public void create() {

    }

}