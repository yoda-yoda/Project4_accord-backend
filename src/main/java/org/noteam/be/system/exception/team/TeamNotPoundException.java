package org.noteam.be.system.exception.team;

public class TeamNotPoundException extends RuntimeException {
    public TeamNotPoundException(String message) {
        super(message);
    }

    public TeamNotPoundException() {
        super();
    }

    public TeamNotPoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TeamNotPoundException(Throwable cause) {
        super(cause);
    }
}
