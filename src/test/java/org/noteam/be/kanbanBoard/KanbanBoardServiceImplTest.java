package org.noteam.be.kanbanBoard;


import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.noteam.be.kanbanBoard.domain.KanbanBoard;
import org.noteam.be.kanbanBoard.dto.*;
import org.noteam.be.kanbanBoard.repository.KanbanBoardRepository;
import org.noteam.be.kanbanBoard.service.KanbanBoardService;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.Role;
import org.noteam.be.member.domain.Status;
import org.noteam.be.member.repository.MemberRepository;
import org.noteam.be.team.domain.Team;
import org.noteam.be.team.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@Rollback(true)
@Transactional
@SpringBootTest
public class KanbanBoardServiceImplTest {

    @Autowired
    private KanbanBoardService kanbanBoardService;

    @Autowired
    private KanbanBoardRepository kanbanBoardRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MemberRepository memberRepository;

    Member member;
    Team team;
    KanbanBoard board1;
    KanbanBoard board2;

    @BeforeEach
    @DisplayName("테스트 시작을 위한 ")
    @Transactional
    void setUp() {

        member = Member.builder()
                .nickname("TestMember")
                .email("TestEmail@gmail.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .provider("GOOGLE")
                .role(Role.MEMBER)
                .status(Status.ACTIVE)
                .build();
        memberRepository.save(member);

        team = Team.builder()
                .teamName("TestTeam")
                .build();
        teamRepository.save(team);

       board1 = KanbanBoard.builder()
                .team(team)
                .priority(1L)
                .title("TestKanbanBoard1")
                .build();
        kanbanBoardRepository.save(board1);

        board2 = KanbanBoard.builder()
                .team(team)
                .priority(2L)
                .title("TestKanbanBoard2")
                .build();
        kanbanBoardRepository.save(board2);


    }

    @Test
    @DisplayName("보드 생성")
    void createKanbanBoard() throws Exception {

        // given
        KanbanBoardCreateRequest request = KanbanBoardCreateRequest.builder()
                .teamId(team.getId())
                .title("test Board")
                .build();

        // when
        KanbanBoardMessageResponse createdBoard = kanbanBoardService.createBoard(request);
        String message = createdBoard.getMessage();
        // then
        Assertions.assertThat(message).isEqualTo("Success Create Board");
        log.info(" createKanbanBoard 테스트 성공: {}", message);

    }

    @Test
    @DisplayName("보드 조회")
    void getKanbanBoard() throws Exception {

        Long priority = 1L;
        String title = "TestTitle";

        // given
        KanbanBoard createdBoard = KanbanBoard.builder()
                .title(title)
                .team(team)
                .priority(priority)
                .build();

        KanbanBoard board = kanbanBoardRepository.save(createdBoard);

        // when
        KanbanBoard kanbanBoardById = kanbanBoardService.getKanbanBoardById(board.getId());

        // then
        assertNotNull(kanbanBoardById, "카드를 정상적으로 조회해야 합니다.");
        assertEquals(board.getId(), kanbanBoardById.getId(), "ID가 일치해야 합니다.");
        assertEquals(board.getTitle(), kanbanBoardById.getTitle(), "제목이 일치해야 합니다.");
        assertEquals(board.getPriority(), kanbanBoardById.getPriority(), "우선순위가 일치해야 합니다.");

        log.info(" getKanbanBoard 테스트 성공: {}", kanbanBoardById);
    }

    @Test
    @DisplayName("보드 수정")
    void updateKanbanBoard() throws Exception {

        Long priority = 1L;
        String title = "TestTitle";
        String updatedTitle = "UpdatedTitle";

        // given
        KanbanBoard createdBoard = KanbanBoard.builder()
                .title(title)
                .team(team)
                .priority(priority)
                .build();

        KanbanBoard board = kanbanBoardRepository.save(createdBoard);

        KanbanBoardUpdateRequest request = KanbanBoardUpdateRequest.builder()
                .title(updatedTitle)
                .boardId(board.getId())
                .build();

        // when
        KanbanBoardMessageResponse response = kanbanBoardService.updateBoard(request);
        String message = response.getMessage();

        // then
        Assertions.assertThat(message).isEqualTo("Success update Board");
        log.info(" updateCard 테스트 성공: {}", message);
    }


    @Test
    @DisplayName("보드 삭제")
    void deleteKanbanBoard() throws Exception {

        Long priority = 1L;
        String title = "TestTitle";
        String updatedTitle = "UpdatedTitle";

        // given
        KanbanBoard createdBoard = KanbanBoard.builder()
                .title(title)
                .team(team)
                .priority(priority)
                .build();

        KanbanBoard board = kanbanBoardRepository.save(createdBoard);

        // when
        KanbanBoardMessageResponse response = kanbanBoardService.deleteBoard(board.getId());
        String message = response.getMessage();
        // then
        Assertions.assertThat(message).isEqualTo("Success Delete Board");
        log.info(" updateCard 테스트 성공: {}", message);
    }

    @Test
    @Transactional
    @DisplayName("보드 위치 변경")
    void changeBoardPriority() throws Exception {
        log.info("😂");
        KanbanBoardSwitchRequest request = KanbanBoardSwitchRequest.builder()
                .boardId(board1.getId())
                .teamId(team.getId())
                .newPriority(2)
                .build();
        log.info("😂request = {}", request);

        KanbanBoardAndCardResponse result = kanbanBoardService.changeBoardPriority(request);
        log.info("😀result = {}", result);
        log.info("result.getKanbanBoards().get(0) = {}", result.getKanbanBoards().get(0));
        log.info("board1.getId() = {}", board1.getId());

        KanbanBoardResponse resultboard1 = result.getKanbanBoards().get(0);
        KanbanBoardResponse resultboard2 = result.getKanbanBoards().get(1);

        assertEquals(resultboard1.getPriority(),1L, "priority가 일치해야합니다");
        assertEquals(resultboard2.getId(), board1.getId(), "ID가 일치해야 합니다." );
        assertEquals(resultboard2.getPriority(), 2L ,"priority가 변경되어야 합니다.");

    }
}
