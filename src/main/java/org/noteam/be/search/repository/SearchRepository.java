package org.noteam.be.search.repository;

import org.noteam.be.joinBoard.domain.JoinBoard;
import org.noteam.be.joinBoard.domain.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRepository extends JpaRepository<JoinBoard, Long> {

    Page<JoinBoard> findByTitleContainingOrTopicContainingAndStatus(String title, String topic, Status status, Pageable pageable);

}
