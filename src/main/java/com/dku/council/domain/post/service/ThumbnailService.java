package com.dku.council.domain.post.service;

import com.dku.council.infra.nhn.s3.model.ImageRequest;
import com.dku.council.infra.nhn.s3.model.OriginalUploadedImage;
import com.dku.council.infra.nhn.s3.model.UploadedFile;
import com.dku.council.infra.nhn.s3.service.FileUploadService;
import com.dku.council.infra.nhn.s3.service.OriginalFileUploadService;
import com.dku.council.infra.nhn.s3.service.ObjectUploadContext;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class ThumbnailService {

    private final ObjectUploadContext uploadContext;

    @Value("${app.post.thumbnail.height-size}")
    private final int heightSize;

    @Value("${app.post.thumbnail.width-size}")
    private final int widthSize;


    public String createThumbnail(OriginalFileUploadService.Context uploadCtx, OriginalUploadedImage file) {
        if (!file.getMimeType().getType().equalsIgnoreCase("image")) {
            return null;
        }

        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            InputStream fileInStream = file.getFile().getInputStream();
            Thumbnails.of(fileInStream)
                    .size(widthSize, heightSize)
                    .outputFormat("png")
                    .toOutputStream(outStream);

            InputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
            ImageRequest req = new ImageRequest(file.getOriginalName(), MediaType.IMAGE_PNG, () -> inStream);

            String thumbnailId = uploadContext.makeObjectId("thumb", "png");
            uploadCtx.originalUploadFileWithName(req, thumbnailId);

            fileInStream.close();
            inStream.close();
            outStream.close();

            return thumbnailId;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String createDefaultThumbnail(FileUploadService.Context uploadCtx, UploadedFile file) {
        return null;
    }
}
