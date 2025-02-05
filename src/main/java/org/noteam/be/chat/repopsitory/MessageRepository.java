package org.noteam.be.chat.repopsitory;

import org.noteam.be.chat.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Optional<Message> findByMessageId(Long id);

    List<Message> findByTeamId(Long teamId);

    Page<Message> findByTeamIdOrderBySendAtDesc(Long teamId, Pageable pageable);

}
