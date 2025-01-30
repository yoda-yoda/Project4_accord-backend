package org.noteam.be.image.service.impl;

import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.noteam.be.image.dto.UploadImageRequest;
import org.noteam.be.image.dto.UploadImageResponse;
import org.noteam.be.system.exception.image.FileSizeExceededException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ImageUploadServiceTest {

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private S3ImageUploadService s3ImageUploadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private MultipartFile mockMultipartFile() {
        return new MockMultipartFile(
                "file",
                "test-image.jpg",
                "image/jpeg",
                new byte[1234]
        );
    }

    @Test
    void uploadImage_shouldUploadImageAndReturnResponse() throws IOException {
        // Initialize S3Mock
        S3Mock api = new S3Mock.Builder().withInMemoryBackend().build();
        api.start(); // Start on port 8001
        String endpoint = "http://localhost:8001";

        // Initialize S3Client with Mock Endpoint
        S3Client mockS3Client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.US_EAST_1)
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create("accessKey", "secretKey")))
                .forcePathStyle(true) // Force path-style addressing
                .build();

        // Create service instance
        S3ImageUploadService s3ImageUploadService = new S3ImageUploadService(mockS3Client);

        // Reflection to set bucketName
        String bucketName = "test-bucket";
        ReflectionTestUtils.setField(s3ImageUploadService, "bucketName", bucketName);
        long maxFileSize = 5 * 1024 * 1024L; // 5 MB
        ReflectionTestUtils.setField(s3ImageUploadService, "maxFileSize", maxFileSize);

        // Create bucket in S3Mock
        mockS3Client.createBucket(b -> b.bucket(bucketName));

        // Given
        MultipartFile mockFile = mockMultipartFile(); // Mock MultipartFile 생성
        UploadImageRequest request = UploadImageRequest.builder()
                .file(mockFile)
                .build();

        System.out.println(request.getFile().getSize());
        System.out.println();

        // When
        UploadImageResponse response = s3ImageUploadService.uploadImage(request);

        // Then
        assertThat(response.getUrl()).contains(bucketName);
        assertThat(response.getFileName()).isNotEmpty();

        // Shutdown the mock server
        api.shutdown();
    }

    @Test
    void deleteImage_shouldDeleteImageFromS3() {
        // Initialize S3Mock
        S3Mock api = new S3Mock.Builder().withInMemoryBackend().build();
        api.start(); // Start on port 8001
        String endpoint = "http://localhost:8001";

        // Initialize S3Client with Mock Endpoint
        S3Client mockS3Client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.US_EAST_1)
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create("accessKey", "secretKey")))
                .forcePathStyle(true) // Force path-style addressing
                .build();

        // Create service instance
        S3ImageUploadService s3ImageUploadService = new S3ImageUploadService(mockS3Client);

        // Reflection to set bucketName
        String bucketName = "test-bucket";
        ReflectionTestUtils.setField(s3ImageUploadService, "bucketName", bucketName);
        long maxFileSize = 5 * 1024 * 1024L; // 5 MB
        ReflectionTestUtils.setField(s3ImageUploadService, "maxFileSize", maxFileSize);

        // Create bucket in S3Mock
        mockS3Client.createBucket(b -> b.bucket(bucketName));

        // Upload a file to mock S3
        String uniqueFileName = "test-file.txt";
        String fileContent = "This is a test file";
        mockS3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(uniqueFileName)
                        .build(),
                RequestBody.fromString(fileContent)
        );

        // Assert that the file exists
        boolean fileExistsBeforeDelete = mockS3Client.listObjectsV2(b -> b.bucket(bucketName))
                .contents()
                .stream()
                .anyMatch(obj -> obj.key().equals(uniqueFileName));
        assertThat(fileExistsBeforeDelete).isTrue();

        // When
        s3ImageUploadService.deleteImage(uniqueFileName);

        // Then
        boolean fileExistsAfterDelete = mockS3Client.listObjectsV2(b -> b.bucket(bucketName))
                .contents()
                .stream()
                .anyMatch(obj -> obj.key().equals(uniqueFileName));
        assertThat(fileExistsAfterDelete).isFalse();

        // Shutdown the mock server
        api.shutdown();
    }

    @Test
    void uploadImage_shouldThrowExceptionWhenFileSizeExceedsLimit() throws IOException {
        // Given
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getSize()).thenReturn(10 * 1024 * 1024L); // 10 MB
        UploadImageRequest request = UploadImageRequest.builder()
                .file(mockFile)
                .build();

        // Reflection to set maxFileSize
        String bucketName = "test-bucket";
        ReflectionTestUtils.setField(s3ImageUploadService, "bucketName", bucketName);
        long maxFileSize = 5 * 1024 * 1024L; // 5 MB
        ReflectionTestUtils.setField(s3ImageUploadService, "maxFileSize", maxFileSize);

        // Expect exception
        assertThrows(FileSizeExceededException.class, () -> s3ImageUploadService.uploadImage(request));
    }


}