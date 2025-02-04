package org.noteam.be.search.repository;

import org.noteam.be.joinBoard.domain.JoinBoard;
import org.noteam.be.joinBoard.domain.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRepository extends JpaRepository<JoinBoard, Long> {

    // (A AND C) OR (B AND C) 같은 형태로 데이터를 찾아온다
    Page<JoinBoard> findByTitleContainingAndStatusOrTopicContainingAndStatus(String title, Status status1, String topic, Status status2, Pageable pageable);

}
