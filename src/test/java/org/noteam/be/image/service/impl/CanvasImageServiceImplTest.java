package org.noteam.be.image.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.noteam.be.image.domain.CanvasImage;
import org.noteam.be.image.dto.UploadCanvasImageRequest;
import org.noteam.be.image.dto.UploadImageRequest;
import org.noteam.be.image.dto.UploadImageResponse;
import org.noteam.be.image.repository.CanvasImageRepository;
import org.noteam.be.image.service.ImageUploadService;
import org.noteam.be.system.exception.image.InvalidFileNameException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CanvasImageServiceImplTest {

    @Mock
    private CanvasImageRepository canvasImageRepository;

    @Mock
    private ImageUploadService imageUploadService;

    @InjectMocks
    private CanvasImageServiceImpl canvasImageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private MultipartFile mockMultipartFile() {
        return new MockMultipartFile(
                "file",
                "test-image.jpg",
                "image/jpeg",
                new byte[]{1, 2, 3}
        );
    }

    @Test
    void saveImage_shouldSaveCanvasImageAndReturnResponse() throws IOException {
        // Given
        UploadCanvasImageRequest request = UploadCanvasImageRequest.builder()
                .canvasId("anystring")
                .file(mockMultipartFile())
                .build();

        UploadImageResponse uploadImageResponse = UploadImageResponse.builder()
                .url("https://s3.amazonaws.com/bucket/test-image.jpg")
                .fileName("test-image.jpg")
                .build();

        when(imageUploadService.uploadImage(any(UploadImageRequest.class)))
                .thenReturn(uploadImageResponse);

        // When
        UploadImageResponse response = canvasImageService.saveImage(request);

        // Then
        assertThat(response.getUrl()).isEqualTo("https://s3.amazonaws.com/bucket/test-image.jpg");
        assertThat(response.getFileName()).isEqualTo("test-image.jpg");
        verify(canvasImageRepository, times(1)).save(any(CanvasImage.class));
    }

    @Test
    void deleteImage_shouldDeleteCanvasImageAndCallImageUploadService() {
        // Given
        String fileName = "test-image.jpg";
        CanvasImage canvasImage = CanvasImage.builder()
                .fileName(fileName)
                .imageUrl("https://s3.amazonaws.com/bucket/test-image.jpg")
                .canvasId("anystring")
                .build();

        when(canvasImageRepository.getCanvasImageByFileName(fileName))
                .thenReturn(Optional.of(canvasImage));

        // When
        canvasImageService.deleteImage(fileName);

        // Then
        verify(canvasImageRepository, times(1)).delete(canvasImage);
        verify(imageUploadService, times(1)).deleteImage(fileName);
    }

    @Test
    void deleteImage_shouldThrowExceptionWhenFileNameIsInvalid() {
        // Given
        String fileName = "invalid-image.jpg";
        when(canvasImageRepository.getCanvasImageByFileName(fileName))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> canvasImageService.deleteImage(fileName))
                .isInstanceOf(InvalidFileNameException.class)
                .hasMessage("유효하지 않은 이미지 파일 이름 입니다.");
        verify(imageUploadService, never()).deleteImage(anyString());
    }

}