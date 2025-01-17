package org.noteam.be.system.exception.member;

public class NicknameAlreadyExist extends RuntimeException {
    public NicknameAlreadyExist(String message) {
        super(message);
    }
}
