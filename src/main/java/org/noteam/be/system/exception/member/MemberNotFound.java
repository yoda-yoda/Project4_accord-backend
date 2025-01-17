package org.noteam.be.system.exception.member;

public class MemberNotFound extends RuntimeException {
    public MemberNotFound(String message) {
        super(message);
    }
}
