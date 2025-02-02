package org.noteam.be.system.exception.jwt;

public class InvalidRefreshTokenProvided extends RuntimeException {
    public InvalidRefreshTokenProvided(String message) {
        super(message);
    }
}
