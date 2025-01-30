package org.noteam.be.system.exception.jwt;

public class InvalidTokenFormatException extends RuntimeException {
    public InvalidTokenFormatException(String message) {
        super(message);
    }
}
