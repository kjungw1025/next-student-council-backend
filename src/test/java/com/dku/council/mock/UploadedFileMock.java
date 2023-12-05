package com.dku.council.mock;

import com.dku.council.infra.nhn.s3.model.ImageRequest;
import com.dku.council.infra.nhn.s3.model.OriginalUploadedImage;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

public class UploadedFileMock {
    public static List<OriginalUploadedImage> createList(int totalFiles) {
        List<OriginalUploadedImage> files = new ArrayList<>(totalFiles);
        for (int i = 1; i <= totalFiles; i++) {
            files.add(new OriginalUploadedImage("file" + i,
                    new ImageRequest("myFile" + i + ".txt", MediaType.TEXT_PLAIN, () -> null)));
        }
        return files;
    }
}
