package org.noteam.be.system.exception.team;

public class TeamNotFoundException extends RuntimeException {
    public TeamNotFoundException(String message) {
        super(message);
    }

    public TeamNotFoundException() {
        super();
    }

    public TeamNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TeamNotFoundException(Throwable cause) {
        super(cause);
    }
}
