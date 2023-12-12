package com.dku.council.domain.post.model.dto.request;

import com.dku.council.domain.post.model.CoalitionType;
import com.dku.council.domain.post.model.entity.posttype.Coalition;
import com.dku.council.domain.user.model.entity.User;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class RequestCreateCoalitionDto extends RequestCreateGenericPostDto<Coalition> {

    private final CoalitionType coalitionType;

    public RequestCreateCoalitionDto(@NotBlank String title, @NotBlank String body, List<Long> tagIds, List<MultipartFile> images, List<MultipartFile> files, @NotNull CoalitionType coalitionType) {
        super(title, body, tagIds, images, files);
        this.coalitionType = coalitionType;
    }

    public Coalition toEntity(User user) {
        return Coalition.builder()
                .body(getBody())
                .title(getTitle())
                .user(user)
                .coalitionType(coalitionType)
                .build();
    }
}
