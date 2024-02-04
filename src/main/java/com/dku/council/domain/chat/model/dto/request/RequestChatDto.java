package com.dku.council.domain.chat.model.dto.request;

import com.dku.council.domain.chat.model.FileType;
import com.dku.council.domain.chat.model.MessageType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RequestChatDto {

    private final MessageType type;

    private final String roomId;

    private final Long userId;

    private final String sender;

    private final String message;

    private final String fileName; // 파일이름
    private final String fileUrl; // s3에 업로드 된 위치
    private final FileType fileType; // 이미지인지 파일인지
}
