package org.noteam.be.image.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.image.domain.NoteImage;
import org.noteam.be.image.dto.UploadImageRequest;
import org.noteam.be.image.dto.UploadImageResponse;
import org.noteam.be.image.dto.UploadNoteImageRequest;
import org.noteam.be.image.repository.NoteImageRepository;
import org.noteam.be.image.service.ImageUploadService;
import org.noteam.be.image.service.NoteImageService;
import org.noteam.be.system.exception.ExceptionMessage;
import org.noteam.be.system.exception.image.InvalidFileNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class NoteImageServiceImpl implements NoteImageService {

    public final ImageUploadService imageUploadService;
    public final NoteImageRepository noteImageRepository;

    @Transactional
    public UploadImageResponse saveImage(UploadNoteImageRequest uploadImageRequest) throws IOException {
        UploadImageResponse uploadImageResponse = imageUploadService.uploadImage(UploadImageRequest.builder()
                .file(uploadImageRequest.getFile())
                .build());

        noteImageRepository.save(NoteImage.builder()
                .noteId(uploadImageRequest.getNoteId())
                .imageUrl(uploadImageResponse.getUrl())
                .fileName(uploadImageResponse.getFileName())
                .build());

        return uploadImageResponse;
    }

    @Transactional
    public void deleteImage(String fileName) {
        imageUploadService.deleteImage(fileName);

    }

    public NoteImage getNoteImageByFileName(String fileName) {
        return noteImageRepository.getNoteImageByFileName(fileName)
                .orElseThrow(() -> new InvalidFileNameException(ExceptionMessage.Image.INVALID_FILENAME_ERROR));
    }

    @Transactional
    public void deleteCanvasImageByFileName(String fileName) {
        noteImageRepository.delete(getNoteImageByFileName(fileName));
    }

}
