package org.noteam.be.image.controller;

import lombok.RequiredArgsConstructor;
import org.noteam.be.image.domain.NoteImage;
import org.noteam.be.image.dto.UploadImageResponse;
import org.noteam.be.image.dto.UploadNoteImageRequest;
import org.noteam.be.image.service.NoteImageService;
import org.noteam.be.system.response.ResponseCode;
import org.noteam.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images/notes")
@RequiredArgsConstructor
public class NoteImageUploadController {

    private final NoteImageService noteImageService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseData<UploadImageResponse>> uploadNoteImage(@RequestParam("file") MultipartFile file, String noteId) throws IOException {
        UploadImageResponse uploadImageResponse = noteImageService.saveImage(UploadNoteImageRequest.builder()
                .file(file)
                .noteId(noteId)
                .build());
        return ResponseData.toResponseEntity(ResponseCode.POST_NOTE_IMAGE_SUCCESS, uploadImageResponse);
    }

    @DeleteMapping
    public ResponseEntity<ResponseData> deleteNoteImage(@RequestBody String noteId) {
        noteImageService.deleteImage(noteId);
        return ResponseData.toResponseEntity(ResponseCode.DELETE_NOTE_IMAGE_SUCCESS);
    }
}
