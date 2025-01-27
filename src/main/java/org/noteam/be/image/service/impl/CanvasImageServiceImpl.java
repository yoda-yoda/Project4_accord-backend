package org.noteam.be.image.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.image.dto.UploadCanvasImageRequest;
import org.noteam.be.image.dto.UploadImageRequest;
import org.noteam.be.image.dto.UploadImageResponse;
import org.noteam.be.image.domain.CanvasImage;
import org.noteam.be.image.repository.CanvasImageRepository;
import org.noteam.be.image.service.CanvasImageService;
import org.noteam.be.image.service.ImageUploadService;
import org.noteam.be.system.exception.ExceptionMessage;
import org.noteam.be.system.exception.image.InvalidFileNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class CanvasImageServiceImpl implements CanvasImageService {

    private final CanvasImageRepository canvasImageRepository;
    private final ImageUploadService imageUploadService;

    @Transactional
    public UploadImageResponse saveImage(UploadCanvasImageRequest uploadImageRequest) throws IOException {
        UploadImageResponse uploadImageResponse = imageUploadService.uploadImage(UploadImageRequest.builder()
                .file(uploadImageRequest.getFile())
                .build());

        canvasImageRepository.save(CanvasImage.builder()
                        .canvasId(uploadImageRequest.getCanvasId())
                        .imageUrl(uploadImageResponse.getUrl())
                        .fileName(uploadImageResponse.getFileName())
                .build());

        return uploadImageResponse;
    }

    @Transactional
    public void deleteImage(String fileName) {
        deleteCanvasImageByFileName(fileName);
        imageUploadService.deleteImage(fileName);

    }

    public CanvasImage getCanvasImageByFileName(String fileName) {
        return canvasImageRepository.getCanvasImageByFileName(fileName)
                .orElseThrow(() -> new InvalidFileNameException(ExceptionMessage.Image.INVALID_FILENAME_ERROR));
    }

    @Transactional
    public void deleteCanvasImageByFileName(String fileName) {
        canvasImageRepository.delete(getCanvasImageByFileName(fileName));
    }

}
