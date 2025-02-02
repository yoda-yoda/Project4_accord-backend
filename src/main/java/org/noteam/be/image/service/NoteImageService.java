package org.noteam.be.image.service;

import org.noteam.be.image.dto.UploadImageResponse;
import org.noteam.be.image.dto.UploadNoteImageRequest;

import java.io.IOException;

public interface NoteImageService {
    public UploadImageResponse saveImage(UploadNoteImageRequest uploadImageRequest) throws IOException;
    public void deleteImage(String fileName);
}
