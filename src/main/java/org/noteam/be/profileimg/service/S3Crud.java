package org.noteam.be.profileimg.service;

import java.io.IOException;

public interface S3Crud {

    String DEFAULT_URL = "https://accord-static.s3.ap-northeast-2.amazonaws.com/Default.jpg";

    String upload(byte[] file, String fileName) throws IOException;

    void delete(String fileName) throws IOException;

}
