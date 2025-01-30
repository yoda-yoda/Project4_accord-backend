package org.noteam.be.system.exception.image;

public class InvalidFileNameException extends RuntimeException {
    public InvalidFileNameException() {
        super();
    }

    public InvalidFileNameException(String message) {
        super(message);
    }

    public InvalidFileNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFileNameException(Throwable cause) {
        super(cause);
    }
}
