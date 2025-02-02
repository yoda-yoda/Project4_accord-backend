package org.noteam.be.image.service;

import org.noteam.be.image.dto.UploadImageRequest;
import org.noteam.be.image.dto.UploadImageResponse;

import java.io.IOException;

public interface ImageUploadService {
    public UploadImageResponse uploadImage(UploadImageRequest uploadImageRequest) throws IOException;
    public void deleteImage(String uniqueFileName);
}
