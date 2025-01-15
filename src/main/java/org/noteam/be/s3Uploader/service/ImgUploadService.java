package org.noteam.be.s3Uploader.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImgUploadService {

    void save(MultipartFile file) throws IOException;

    void setDefault() throws IOException;

}
