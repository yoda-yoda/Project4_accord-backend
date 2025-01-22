package org.noteam.be.image.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.image.dto.UploadImageRequest;
import org.noteam.be.image.dto.UploadImageResponse;
import org.noteam.be.image.service.ImageUploadService;
import org.noteam.be.system.exception.ExceptionMessage;
import org.noteam.be.system.exception.image.FileSizeExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class S3ImageUploadService implements ImageUploadService {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.max-file-size}")
    private long maxFileSize;

    private final S3Client s3Client;

    public UploadImageResponse uploadImage(UploadImageRequest uploadImageRequest) throws IOException {
        if (uploadImageRequest.getFile().getSize() > maxFileSize) {
            throw new FileSizeExceededException(ExceptionMessage.Image.FILE_SIZE_EXCEEDED_ERROR);
        }

        String uniqueFileName = UUID.randomUUID() + "_" + uploadImageRequest.getFile().getOriginalFilename();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(uniqueFileName)
                .build();
        s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(uploadImageRequest.getFile().getBytes()));
        URL fileUrl = s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(uniqueFileName));
        return UploadImageResponse.builder()
                .url(fileUrl.toString())
                .fileName(uniqueFileName)
                .build();
    }

    public void deleteImage(String uniqueFileName) {
        s3Client.deleteObject(builder -> builder.bucket(bucketName).key(uniqueFileName));
    }


}
