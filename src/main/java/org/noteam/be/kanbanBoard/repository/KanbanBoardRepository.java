package org.noteam.be.kanbanBoard.repository;

import org.noteam.be.kanbanBoard.domain.KanbanBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KanbanBoardRepository extends JpaRepository<KanbanBoard, Long> {
}
