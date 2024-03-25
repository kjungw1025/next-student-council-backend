package com.dku.council.domain.danfesta.service;

import com.dku.council.domain.danfesta.model.FestivalDate;
import com.dku.council.domain.danfesta.model.dto.request.RequestCreateLineUpDto;
import com.dku.council.domain.danfesta.model.dto.response.ResponseLineUpDto;
import com.dku.council.domain.danfesta.model.entity.LineUp;
import com.dku.council.domain.danfesta.model.entity.LineUpImage;
import com.dku.council.domain.danfesta.repository.LineUpRepository;
import com.dku.council.domain.danfesta.repository.spec.LineUpSpec;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.global.error.exception.NotGrantedException;
import com.dku.council.global.error.exception.UserNotFoundException;
import com.dku.council.infra.nhn.s3.model.ImageRequest;
import com.dku.council.infra.nhn.s3.model.UploadedImage;
import com.dku.council.infra.nhn.s3.service.ImageUploadService;
import com.dku.council.infra.nhn.s3.service.ObjectUploadContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LineUpService {

    private final LineUpRepository lineUpRepository;
    private final UserRepository userRepository;

    private final ImageUploadService imageUploadService;
    private final ObjectUploadContext context;

    @Transactional
    public Long createLineUp(RequestCreateLineUpDto dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if(!user.getUserRole().isAdmin()) {
            throw new NotGrantedException();
        }
        LineUp lineUp = LineUp.builder()
                .singer(dto.getSinger())
                .description(dto.getDescription())
                .performanceTime(convertToLocalDate(dto.getPerformanceDate()))
                .festivalDate(dto.getFestivalDate())
                .isOpened(false)
                .build();
        addLineUpImages(lineUp, dto.getImages());

        LineUp result = lineUpRepository.save(lineUp);
        return result.getId();
    }

    private void addLineUpImages(LineUp lineUp, List<MultipartFile> dtoImages) {
        List<UploadedImage> images = imageUploadService.newContext().uploadImages(
                ImageRequest.ofList(dtoImages),
                lineUp.getClass().getSimpleName());

        List<LineUpImage> lineUpImages = new ArrayList<>();

        for (UploadedImage image : images) {
            LineUpImage.LineUpImageBuilder builder = LineUpImage.builder()
                    .fileName(image.getOriginalName())
                    .mimeType(image.getMimeType().toString())
                    .fileId(image.getImageId());

            lineUpImages.add(builder.build());
        }

        for (LineUpImage image : lineUpImages) {
            image.changeLineUp(lineUp);
        }
    }

    private LocalDate convertToLocalDate(String performanceDate) {
        String[] split = performanceDate.split("-");
        return LocalDate.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
    }

    @Transactional(readOnly = true)
    public List<ResponseLineUpDto> list(FestivalDate festivalDate) {
        Specification<LineUp> spec = LineUpSpec.withFestivalDate(festivalDate);
        List<LineUp> list = lineUpRepository.findAll(spec);

        return list.stream().map(line -> {
            if (!line.isOpened()){
                LineUp lineUp = LineUp.builder()
                        .singer("공개 예정")
                        .description("공개 예정")
                        .performanceTime(null)
                        .festivalDate(null)
                        .isOpened(false)
                        .build();
                lineUp.setDefaultImages();
                return new ResponseLineUpDto(context, lineUp);
            } else {
                return new ResponseLineUpDto(context, line);
            }
        }).collect(Collectors.toList());
    }

    @Transactional
    public void changeToTrue(Long userId, FestivalDate festivalDate) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (!user.getId().equals(userId) || !user.getUserRole().isAdmin()) {
            throw new NotGrantedException();
        }

        List<LineUp> list = lineUpRepository.findAllByFestivalDate(festivalDate);
        for(LineUp lineUp : list) {
            lineUp.changeIsOpened();
            lineUpRepository.save(lineUp);
        }
    }
}
