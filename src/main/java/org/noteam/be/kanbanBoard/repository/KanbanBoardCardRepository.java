package org.noteam.be.kanbanBoard.repository;

import org.noteam.be.kanbanBoard.domain.KanbanBoardCard;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface KanbanBoardCardRepository extends JpaRepository<KanbanBoardCard, Long> {

    Optional<KanbanBoardCard> findByid(Long id);

    List<KanbanBoardCard> findByBoardId(Long boardId, Sort sort);


}
