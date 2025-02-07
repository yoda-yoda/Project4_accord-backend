package org.noteam.be.profileimg.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.noteam.be.profileimg.service.ProfileImgService;
import org.springframework.http.ResponseEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "프로필이미지", description = "프로필 이미지 등록/삭제 API")
@RestController
@RequestMapping("/api/members/profile-images")
@RequiredArgsConstructor
public class S3UploaderController {

    private final ProfileImgService profileImgService;


    @Operation(summary = "프로필 이미지 등록", description = "기존 프로필 이미지가 있다면 대체해서 등록됨")
    @PostMapping
    public ResponseEntity<String> profileImageUploader(@RequestParam("file") @NotNull MultipartFile file) throws IOException {

        profileImgService.upload(file);

        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "프로필 이미지 삭제", description = "기존 프로필 이미지를 삭제하고 기본제공 이미지로 대체")
    @DeleteMapping
    public ResponseEntity<String> defaultImageUploader() throws IOException {

        profileImgService.setDefault();

        return ResponseEntity.noContent().build();
    }

}
