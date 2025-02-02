package org.noteam.be.system.exception.member;

public class BannedAccountException extends RuntimeException {
    public BannedAccountException(String message) {
        super(message);
    }
}
