package com.dku.council.domain.post.model.dto.request;

import com.dku.council.domain.post.model.entity.posttype.Notice;
import com.dku.council.domain.user.model.entity.User;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
public class RequestCreateNoticeDto extends RequestCreateGenericPostDto<Notice> {

    public RequestCreateNoticeDto(@NotBlank String title, @NotBlank String body, List<Long> tagIds, List<MultipartFile> images, List<MultipartFile> files) {
        super(title, body, tagIds, images, files);
    }

    public Notice toEntity(User user) {
        return Notice.builder()
                .body(getBody())
                .title(getTitle())
                .user(user)
                .build();
    }
}
