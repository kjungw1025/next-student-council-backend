package com.dku.council.domain.admin.service;

import com.dku.council.domain.admin.dto.AdBannerPageDto;
import com.dku.council.domain.banner.exception.BannerNotFoundException;
import com.dku.council.domain.banner.exception.InvalidBannerTypeException;
import com.dku.council.domain.banner.model.BannerImage;
import com.dku.council.domain.banner.model.dto.request.RequestCreateBannerDto;
import com.dku.council.domain.banner.model.dto.response.ResponseBannerDto;
import com.dku.council.domain.banner.model.entity.Banner;
import com.dku.council.domain.banner.repository.BannerImageRepository;
import com.dku.council.domain.banner.repository.BannerRepository;
import com.dku.council.infra.nhn.s3.model.ImageRequest;
import com.dku.council.infra.nhn.s3.service.ImageUploadService;
import com.dku.council.infra.nhn.s3.service.ObjectUploadContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdBannerPageService {

    private final ObjectUploadContext uploadContext;
    private final ImageUploadService imageUploadService;

    private final BannerRepository bannerRepository;
    private final BannerImageRepository bannerImageRepository;

    public List<AdBannerPageDto> getAdBanner() {
        return bannerRepository.findAll().stream()
                .map(banner -> {
                    BannerImage bannerImage = bannerImageRepository.findByBannerId(banner.getId())
                            .orElseThrow(BannerNotFoundException::new);
                    return new AdBannerPageDto(banner, bannerImage, uploadContext);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void createBanner(RequestCreateBannerDto dto) {
        MultipartFile file = dto.getImage();
        String redirectUrl = dto.getBannerUrl();
        String originalImageName = file.getOriginalFilename();

        if (!verifyImageExtension(originalImageName)) {
            throw new InvalidBannerTypeException(originalImageName);
        }

        String imageId = imageUploadService.newContext()
                .uploadImage(new ImageRequest(file), "banner")
                .getImageId();

        Banner banner = Banner.builder()
                .bannerUrl(redirectUrl)
                .build();

        BannerImage bannerImage = BannerImage.builder()
                .fileId(imageId)
                .mimeType(file.getContentType())
                .fileName(originalImageName)
                .build();
        bannerImage.changeBanner(banner);

        bannerRepository.save(banner);
    }

    private boolean verifyImageExtension(String originName) {
        if (originName == null) return false;

        return originName.endsWith(".jpg") ||
                originName.endsWith(".jpeg") ||
                originName.endsWith(".png") ||
                originName.endsWith(".gif") ||
                originName.endsWith(".svg") ||
                originName.endsWith(".webp");
    }

    @Transactional
    public void deleteBanner(Long adBannerId) {
        Banner banner = bannerRepository.findById(adBannerId)
                .orElseThrow(BannerNotFoundException::new);
        String fileId = banner.getImages().get(0).getFileId();

        imageUploadService.newContext().deleteImage(fileId);
        bannerRepository.delete(banner);
    }

}
