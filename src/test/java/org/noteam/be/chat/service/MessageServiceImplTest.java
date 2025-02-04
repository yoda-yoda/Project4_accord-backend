package org.noteam.be.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.noteam.be.chat.domain.Message;
import org.noteam.be.chat.dto.MessageChunkRequest;
import org.noteam.be.chat.dto.MessageResponse;
import org.noteam.be.chat.repopsitory.MessageRepository;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.Role;
import org.noteam.be.member.domain.Status;
import org.noteam.be.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@DisplayName("메세지 서비스 테스트")
class MessageServiceImplTest {


    private LocalDateTime fixedLocalTime;
    private Member member;
    private Message testMessage;
    private Long testTeamId;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MessageRepository messageRepository;


    //데이터 입력용
    @BeforeEach
    void setUp() {

        //테스트용 데이터 설정
        testTeamId = 1L;
        fixedLocalTime = LocalDateTime.of(2025, 1, 27, 14, 35, 0);

        //test 멤버
        member = Member.builder()
                .email("user@example.com")
                .nickname("user1")
                .createdAt(fixedLocalTime)
                .provider("Provider")
                .role(Role.MEMBER)
                .status(Status.ACTIVE)
                .updatedAt(fixedLocalTime)
                .createdAt(fixedLocalTime)
                .build();

        memberRepository.save(member);

        //Test message 생성, 저장.
        for (int i = 0; i < 100; i++) {

            testMessage = Message.builder()
                        .senderMember(member)
                        .message("Test 임")
                        .teamId(testTeamId)
                        .sendAt(fixedLocalTime)
                        .build();

            messageRepository.save(testMessage);
        }

    }

    @Test
    @DisplayName("무한스크롤 테스트")
    void scrollTest() throws Exception{

        int validateTargetValue;

        for (int i = 0; i < 5; i++) {
            //given
            MessageChunkRequest msgChReq = MessageChunkRequest.builder()
                    .teamId(testTeamId)
                    .chunkNumber(i)
                    .build();

            //when
            Page<MessageResponse> msgPage = messageService.requestChunkMessages(msgChReq);
            validateTargetValue = msgPage.getSize();

            //Then
            log.info("validateTargetValue = {}", validateTargetValue);
            assertThat(validateTargetValue).isLessThanOrEqualTo(20);
        }

    }



}