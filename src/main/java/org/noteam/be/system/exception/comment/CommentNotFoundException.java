package org.noteam.be.system.exception.comment;

public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException(String message) {
        super(message);
    }

    public CommentNotFoundException() {
        super();
    }

    public CommentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommentNotFoundException(Throwable cause) {
        super(cause);
    }
}
