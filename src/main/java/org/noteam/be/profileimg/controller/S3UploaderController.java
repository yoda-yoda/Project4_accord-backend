package org.noteam.be.profileimg.controller;


import lombok.RequiredArgsConstructor;
import org.noteam.be.profileimg.service.ProfileImgService;
import org.springframework.http.ResponseEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/members/profile-images")
@RequiredArgsConstructor
public class S3UploaderController {

    private final ProfileImgService profileImgService;

    @PostMapping
    public ResponseEntity<String> profileImageUploader(@RequestParam("file") @NotNull MultipartFile file) throws IOException {

        profileImgService.upload(file);

        return ResponseEntity.noContent().build();
    }


    @DeleteMapping
    public ResponseEntity<String> defaultImageUploader() throws IOException {

        profileImgService.setDefault();

        return ResponseEntity.noContent().build();
    }

}
