package org.noteam.be.s3Uploader.controller;


import lombok.RequiredArgsConstructor;
import org.noteam.be.s3Uploader.service.ImgUploadService;
import org.noteam.be.system.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/members/profileImages")
@RequiredArgsConstructor
public class S3UploaderController {

    private final ImgUploadService imgUploadService ;

    @PostMapping
    public ResponseEntity<String> s3Uploader(@RequestParam("file") @NotNull MultipartFile file) throws IOException {

        imgUploadService.save(file);

        return ResponseEntity.ok().body("OK");
    }


    @DeleteMapping
    public ResponseEntity<String> defaultS3Uploader() throws IOException {

        imgUploadService.setDefault();

        return ResponseEntity.ok().body("OK");
    }

}
