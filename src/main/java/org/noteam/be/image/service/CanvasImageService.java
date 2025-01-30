package org.noteam.be.image.service;

import org.noteam.be.image.dto.UploadCanvasImageRequest;
import org.noteam.be.image.dto.UploadImageResponse;

import java.io.IOException;

public interface CanvasImageService {
    public UploadImageResponse saveImage(UploadCanvasImageRequest uploadImageRequest) throws IOException;
    public void deleteImage(String fileName);
}
