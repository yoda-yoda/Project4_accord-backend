package org.noteam.be.system.exception.member;

public class EmailNotProvided extends RuntimeException {
    public EmailNotProvided(String message) {
        super(message);
    }
}
