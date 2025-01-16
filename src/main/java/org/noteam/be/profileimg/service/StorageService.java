package org.noteam.be.profileimg.service;

import java.io.IOException;

public interface StorageService {

    String upload(byte[] file, String fileName) throws IOException;

    void delete(String fileName) throws IOException;

    String getDefaultPath();
}
