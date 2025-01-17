package org.noteam.be.system.exception.member;

public class ExistingAuthenticationIsNull extends RuntimeException {
    public ExistingAuthenticationIsNull(String message) {
        super(message);
    }
}
