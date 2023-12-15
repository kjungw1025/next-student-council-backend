package com.dku.council.domain.with_dankook.model.dto.request;

import com.dku.council.domain.user.model.entity.User;
import lombok.Getter;

@Getter
public abstract class RequestCreateWithDankookDto<T> {

    public abstract T toEntity(User user);
}
