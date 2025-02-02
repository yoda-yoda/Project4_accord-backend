package org.noteam.be.image.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.noteam.be.image.domain.NoteImage;
import org.noteam.be.image.dto.UploadImageRequest;
import org.noteam.be.image.dto.UploadImageResponse;
import org.noteam.be.image.dto.UploadNoteImageRequest;
import org.noteam.be.image.repository.NoteImageRepository;
import org.noteam.be.image.service.ImageUploadService;
import org.noteam.be.system.exception.image.InvalidFileNameException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NoteImageServiceImplTest {

    @Mock
    private ImageUploadService imageUploadService;

    @Mock
    private NoteImageRepository noteImageRepository;

    @InjectMocks
    private NoteImageServiceImpl noteImageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private MultipartFile mockMultipartFile() {
        return new MockMultipartFile(
                "file",
                "test-note-image.jpg",
                "image/jpeg",
                new byte[]{1, 2, 3}
        );
    }

    @Test
    void saveImage_shouldSaveNoteImageAndReturnResponse() throws IOException {
        // Given
        UploadNoteImageRequest request = UploadNoteImageRequest.builder()
                .noteId("anystring")
                .file(mockMultipartFile())
                .build();

        UploadImageResponse uploadImageResponse = UploadImageResponse.builder()
                .url("https://s3.amazonaws.com/bucket/test-note-image.jpg")
                .fileName("test-note-image.jpg")
                .build();

        when(imageUploadService.uploadImage(any(UploadImageRequest.class)))
                .thenReturn(uploadImageResponse);

        // When
        UploadImageResponse response = noteImageService.saveImage(request);

        // Then
        assertThat(response.getUrl()).isEqualTo("https://s3.amazonaws.com/bucket/test-note-image.jpg");
        assertThat(response.getFileName()).isEqualTo("test-note-image.jpg");
        verify(noteImageRepository, times(1)).save(any(NoteImage.class));
    }

    @Test
    void getNoteImageByFileName_shouldThrowExceptionWhenFileNameIsInvalid() {
        // Given
        String fileName = "invalid-note-image.jpg";
        when(noteImageRepository.getNoteImageByFileName(fileName))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> noteImageService.getNoteImageByFileName(fileName))
                .isInstanceOf(InvalidFileNameException.class)
                .hasMessage("유효하지 않은 이미지 파일 이름 입니다.");
    }

    @Test
    void deleteCanvasImageByFileName_shouldDeleteNoteImage() {
        // Given
        String fileName = "test-note-image.jpg";
        NoteImage noteImage = NoteImage.builder()
                .fileName(fileName)
                .imageUrl("https://s3.amazonaws.com/bucket/test-note-image.jpg")
                .noteId("anystring")
                .build();

        when(noteImageRepository.getNoteImageByFileName(fileName))
                .thenReturn(Optional.of(noteImage));

        // When
        noteImageService.deleteCanvasImageByFileName(fileName);

        // Then
        verify(noteImageRepository, times(1)).delete(noteImage);
    }

}