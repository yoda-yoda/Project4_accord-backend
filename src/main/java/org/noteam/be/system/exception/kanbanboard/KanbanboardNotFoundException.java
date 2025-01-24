package org.noteam.be.system.exception.kanbanboard;

public class KanbanboardNotFoundException extends RuntimeException {
  public KanbanboardNotFoundException(String message) {
    super(message);
  }
}
