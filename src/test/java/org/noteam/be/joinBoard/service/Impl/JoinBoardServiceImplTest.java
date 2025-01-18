package org.noteam.be.joinBoard.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.noteam.be.joinBoard.dto.JoinBoardRegisterRequest;
import org.noteam.be.joinBoard.dto.JoinBoardResponse;
import org.noteam.be.joinBoard.dto.JoinBoardUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@SpringBootTest
@Slf4j
class JoinBoardServiceImplTest {

    @Autowired
    JoinBoardServiceImpl joinBoardService;

    JoinBoardResponse joinBoardResponse;

    @BeforeEach
    void setUp1() {

        JoinBoardRegisterRequest title = JoinBoardRegisterRequest.builder()
                .title("title")
                .topic("topic")
                .teamName("teamName")
                .projectBio("projectBio")
                .teamBio("teamBio")
                .content("content")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .peopleNumber(3)
                .build();

        joinBoardResponse = joinBoardService.createJoinBoardByDto(title);

    }



    @Test
    @DisplayName("임시테스트")
    void 임시텟ㅌ() throws Exception {

        JoinBoardUpdateRequest build = JoinBoardUpdateRequest.builder()
                .title("t")
                .topic("t")
                .teamName("t")
                .projectBio("t")
                .teamBio("t")
                .content("t")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .peopleNumber(1)
                .build();

        JoinBoardResponse res = joinBoardService.updateJoinBoardById(joinBoardResponse.getId(), build);

        log.info("res.getTitle() = {}", res.getTitle());
        log.info("res.getTopic() = {}", res.getTopic());

    }

}