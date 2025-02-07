package org.noteam.be.image.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "팀 캔버스", description = "팀 캔버스 API")
@RestController
@RequestMapping("/api/images/canvases")
@RequiredArgsConstructor
public class CanvasImageUploadController {

    private final CanvasImageService canvasImageService;

    @Operation(summary = "캔버스 파일 저장", description = "AWS S3 로 캔버스 파일 저장")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseData<UploadImageResponse>> uploadImage(@RequestParam("file") MultipartFile file, String canvasId) throws IOException {
        UploadImageResponse uploadImageResponse = canvasImageService.saveImage(UploadCanvasImageRequest.builder()
                .file(file)
                .canvasId(canvasId)
                .build());
        return ResponseData.toResponseEntity(ResponseCode.POST_CANVAS_IMAGE_SUCCESS, uploadImageResponse);
    }

    @Operation(summary = "캔버스 파일 삭제", description = "현재 사용하지 않음")
    @DeleteMapping
    public ResponseEntity<ResponseData> deleteImage(@RequestParam String fileName) {
        canvasImageService.deleteImage(fileName);
        return ResponseData.toResponseEntity(ResponseCode.DELETE_CANVAS_IMAGE_SUCCESS);
    }

}
