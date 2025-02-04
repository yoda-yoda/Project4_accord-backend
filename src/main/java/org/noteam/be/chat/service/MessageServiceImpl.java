package org.noteam.be.chat.service;

import java.util.List;
import java.util.ArrayList;

import org.noteam.be.chat.dto.MessageChunkRequest;
import org.noteam.be.chat.dto.MessageRequest;
import org.noteam.be.chat.dto.MessageResponse;
import org.noteam.be.chat.domain.Message;
import org.noteam.be.chat.repopsitory.MessageRepository;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.service.MemberService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageServiceImpl implements MessageService {

    @Value("${chatting.chunk.size}") //현재 Size 값은 50임.
    private int CHAT_CHUNK_SIZE;

    private final MessageRepository messageRepository;
    private final MemberService memberService;

    @Override
    @Transactional
    public MessageResponse sendMessage(MessageRequest messageRequest) {

        //발행한 멤버를 찾는다.
        Member sendMember =  memberService.findMemberByMemberId(messageRequest.getSenderId());

        //Entity 변환
        Message msg = Message.of(messageRequest,sendMember);

        //저장
        msg = messageSave(msg);

        //저장된 Entity 를 다시 Request 객체로 변경되어 반환.
        return MessageResponse.from(msg);

    }

    @Override
    @Transactional
    public List<MessageResponse> requestAllMessages(Long teamId) {

        List<Message> Messages =  messageRepository.findByTeamId(teamId);

        List<MessageResponse> msgRsp = new ArrayList<>();

        for (Message msg : Messages) {
            msgRsp.add(MessageResponse.from(msg));
        }

        return msgRsp;
    }

    @Override
    public Page<MessageResponse> requestChunkMessages(MessageChunkRequest msgChunkReq) {

        Pageable pageable = PageRequest.of(msgChunkReq.getChunkNumber(), CHAT_CHUNK_SIZE);

        Page<Message> messages = messageRepository.findByTeamIdOrderBySendAtDesc(msgChunkReq.getTeamId(), pageable);

        //Page 의 content 를 Response 로 바꿈
        return messages.map(MessageResponse::from);
    }

    @Transactional
    public Message messageSave(Message message){
        return messageRepository.save(message);
    }

}
