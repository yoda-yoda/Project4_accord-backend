package org.noteam.be.joinBoard.repository;

import org.noteam.be.joinBoard.domain.JoinBoard;
import org.noteam.be.joinBoard.domain.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinBoardRepository extends JpaRepository<JoinBoard, Long> {

    Page<JoinBoard> findByStatus(Status status, Pageable pageable);

}
