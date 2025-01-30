package org.noteam.be.system.exception.kanbanboard;

public class KanbanboardCardNotFoundException extends RuntimeException {
  public KanbanboardCardNotFoundException(String message) {
    super(message);
  }
}
