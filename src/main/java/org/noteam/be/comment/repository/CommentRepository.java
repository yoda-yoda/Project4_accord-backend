package org.noteam.be.comment.repository;
import org.noteam.be.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByJoinBoardId(Long joinBoardId);

}
