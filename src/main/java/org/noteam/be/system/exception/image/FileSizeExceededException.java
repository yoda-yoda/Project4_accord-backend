package org.noteam.be.system.exception.image;

public class FileSizeExceededException extends RuntimeException {
    public FileSizeExceededException() {
        super();
    }

    public FileSizeExceededException(String message) {
        super(message);
    }

    public FileSizeExceededException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileSizeExceededException(Throwable cause) {
        super(cause);
    }
}
