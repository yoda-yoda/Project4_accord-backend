package org.noteam.be.system.exception.jwt;

public class PublicKeyNotFoundException extends RuntimeException {
    public PublicKeyNotFoundException(String message) {
        super(message);
    }
}
