package org.noteam.be.system.exception.member;

public class EmptyRefreshToken extends RuntimeException {
    public EmptyRefreshToken(String message) {
        super(message);
    }
}
