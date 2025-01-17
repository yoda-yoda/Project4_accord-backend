package org.noteam.be.system.exception.member;

public class InvalidRefreshTokenProvided extends RuntimeException {
    public InvalidRefreshTokenProvided(String message) {
        super(message);
    }
}
