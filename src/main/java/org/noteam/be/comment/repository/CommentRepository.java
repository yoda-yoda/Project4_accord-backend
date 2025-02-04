package org.noteam.be.comment.repository;
import org.noteam.be.joinBoard.domain.JoinBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<JoinBoard, Long> {
    
}
