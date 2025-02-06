package org.noteam.be.chat.service;

import org.noteam.be.chat.dto.MessageChunkRequest;
import org.noteam.be.chat.dto.MessageRequest;
import org.noteam.be.chat.dto.MessageResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MessageService {

    MessageResponse sendMessage(MessageRequest messageRequest);

    List<MessageResponse> requestAllMessages(Long teamId);

    Page<MessageResponse> requestChunkMessages(MessageChunkRequest messageChunkRequest);
}
