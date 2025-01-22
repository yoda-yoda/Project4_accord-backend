package org.noteam.be.system.exception.joinBoard;

public class JoinBoardNotFoundException extends RuntimeException {

    public JoinBoardNotFoundException(String message) {
        super(message);
    }

    public JoinBoardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public JoinBoardNotFoundException(Throwable cause) {
        super(cause);
    }

    public JoinBoardNotFoundException() {
    }
}
