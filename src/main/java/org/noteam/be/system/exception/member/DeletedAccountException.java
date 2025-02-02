package org.noteam.be.system.exception.member;

public class DeletedAccountException extends RuntimeException {
    public DeletedAccountException(String message) {
        super(message);
    }
}
