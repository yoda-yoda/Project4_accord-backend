package org.noteam.be.image.controller;

import lombok.RequiredArgsConstructor;
import org.noteam.be.image.dto.UploadCanvasImageRequest;
import org.noteam.be.image.dto.UploadImageResponse;
import org.noteam.be.image.service.CanvasImageService;
import org.noteam.be.system.response.ResponseCode;
import org.noteam.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images/canvases")
@RequiredArgsConstructor
public class CanvasImageUploadController {

    private final CanvasImageService canvasImageService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseData<UploadImageResponse>> uploadImage(@RequestParam("file") MultipartFile file, String canvasId) throws IOException {
        UploadImageResponse uploadImageResponse = canvasImageService.saveImage(UploadCanvasImageRequest.builder()
                .canvasId(canvasId)
                .build());
        return ResponseData.toResponseEntity(ResponseCode.POST_CANVAS_IMAGE_SUCCESS, uploadImageResponse);
    }

    @DeleteMapping
    public ResponseEntity<ResponseData> deleteImage(@RequestParam String fileName) {
        canvasImageService.deleteImage(fileName);
        return ResponseData.toResponseEntity(ResponseCode.DELETE_CANVAS_IMAGE_SUCCESS);
    }

}
