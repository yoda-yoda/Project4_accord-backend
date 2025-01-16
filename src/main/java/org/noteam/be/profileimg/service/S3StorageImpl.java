package org.noteam.be.profileimg.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.IOException;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class S3StorageImpl implements StorageService {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.default-url}")
    private String defaultUrl;

    private final S3Client s3Client;

    @Override
    public String upload(byte[] file, String fileName) throws IOException {

        //파일 저장단.
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .contentType("image/jpeg")
                        .build(),
                RequestBody.fromBytes(file));

        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;
    }

    @Override
    public void delete(String fileName) throws IOException {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                .build());
    }

    @Override
    public String getDefaultPath() {
        return this.defaultUrl;
    }

}
