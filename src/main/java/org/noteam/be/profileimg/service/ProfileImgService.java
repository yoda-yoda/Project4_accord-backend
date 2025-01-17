package org.noteam.be.profileimg.service;


import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface ProfileImgService {

    void upload(MultipartFile file) throws IOException;

    void setDefault() throws IOException;

}
