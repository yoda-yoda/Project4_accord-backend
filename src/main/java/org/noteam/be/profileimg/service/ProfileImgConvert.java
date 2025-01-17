package org.noteam.be.profileimg.service;

import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ProfileImgConvert {

    int VALID_MAX_WIDTH = 1024;
    int VALID_MAX_HEIGHT = 1024;

    int MAX_WIDTH = 48;
    int MAX_HEIGHT = 48;

    int MAX_SIZE = 10 * 1024 * 1024; //10MB

    byte[] convert(MultipartFile file) throws IOException;

//    BufferedImage checkValidImg(MultipartFile file) throws IOException;
//
//    BufferedImage cropAndResizeImage(BufferedImage image);

}
