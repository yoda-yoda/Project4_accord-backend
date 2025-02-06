package org.noteam.be.kanbanBoard;


import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.noteam.be.kanbanBoard.domain.KanbanBoard;
import org.noteam.be.kanbanBoard.domain.KanbanBoardCard;
import org.noteam.be.kanbanBoard.dto.*;
import org.noteam.be.kanbanBoard.repository.KanbanBoardCardRepository;
import org.noteam.be.kanbanBoard.repository.KanbanBoardRepository;
import org.noteam.be.kanbanBoard.service.KanbanBoardCardService;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(true)
@Slf4j
public class KanbanBoardCardServiceImplTest {

    @Autowired
    private KanbanBoardCardRepository kanbanBoardCardRepository;

    @Autowired
    private KanbanBoardCardService kanbanBoardCardService;

    @Autowired
    private KanbanBoardService kanbanBoardService;


    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private KanbanBoardRepository kanbanBoardRepository;

    Team team;
    Member member;
    KanbanBoard board;
    KanbanBoardCard card1;
    KanbanBoardCard card2;

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

        board = KanbanBoard.builder()
                .team(team)
                .title("TestBoard")
                .priority(1L)
                .build();
        kanbanBoardRepository.save(board);

        card1 = KanbanBoardCard.builder()
                .content("testCard1")
                .member(member)
                .board(board)
                .priority(1L)
                .build();
        kanbanBoardCardRepository.save(card1);


        card2 = KanbanBoardCard.builder()
                .content("testCard1")
                .member(member)
                .board(board)
                .priority(2L)
                .build();
        kanbanBoardCardRepository.save(card2);

        // 방향성 문제 다시 공부
        // db랑 orm은 다르다
        // 1차 캐시때문에 찾아온 인스턴스에 카드를 따로 추가 해준다. add

        board.getCards().add(card1);
        board.getCards().add(card2);



        board = kanbanBoardRepository.findById(board.getId()).orElseThrow();

    }

    @Test
    @DisplayName("카드 생성")
    void createKanbanBoardCard() throws Exception {

        // given
        Long priority = 3L;
        String content = "TestContent";

        // when
        KanbanBoardCard createdCard = kanbanBoardCardService.create(content, member, board, priority);

        // then
        assertNotNull(createdCard);
        assertEquals(content, createdCard.getContent());
        assertEquals(member, createdCard.getMember());
        assertEquals(board, createdCard.getBoard());
        assertEquals(priority, createdCard.getPriority());

        List<KanbanBoardCard> savedCards = kanbanBoardCardRepository.findAll();
        assertFalse(savedCards.isEmpty(), "저장된 카드가 있어야 합니다.");
        assertTrue(savedCards.contains(createdCard), "생성된 카드가 저장소에 있어야 합니다.");

        log.info("KanbanBoardCard 테스트 성공: {}", createdCard);

    }

    @Test
    @DisplayName("카드 조회")
    void getKanbanBoardCard() throws Exception {

        // given
        Long priority = 3L;
        String content = "TestContent";

        KanbanBoardCard createdCard = kanbanBoardCardService.create(content, member, board, priority);

        // when
        KanbanBoardCard getCard = kanbanBoardCardService.getKanbanBoardCard(createdCard.getId());

        // then
        assertNotNull(getCard, "카드를 정상적으로 조회해야 합니다.");
        assertEquals(createdCard.getId(), getCard.getId(), "ID가 일치해야 합니다.");
        assertEquals(content, getCard.getContent(), "내용이 일치해야 합니다.");
        assertEquals(priority, getCard.getPriority(), "우선순위가 일치해야 합니다.");
        assertEquals(member, getCard.getMember(), "작성자가 일치해야 합니다.");
        assertEquals(board, getCard.getBoard(),"연결된 보드가 일치해야 합니다.");

        log.info(" KanbanBoardCard 조회 테스트 성공: {}", getCard);
    }


    @Test
    @DisplayName("카드 수정")
    void updateKanbanBoardCard() throws Exception {


        KanbanBoardCard getCard = kanbanBoardCardService.getKanbanBoardCard(1L);

        KanbanBoardCardUpdateRequest updateRequest = KanbanBoardCardUpdateRequest.builder()
                .cardId(getCard.getId())
                .content("updated content")
                .build();

        // when
        KanbanBoardMessageResponse updatedCard = kanbanBoardCardService.updateCard(updateRequest);
        String message = updatedCard.getMessage();

        // then
        Assertions.assertThat(message).isEqualTo("Sucess Card Update");
        log.info(" updateCard 테스트 성공: {}", message);
    }


    // 일단 조금 있다가 하자 ...
    @Test
    @DisplayName("카드 위치 변경")
    @Transactional
    void changeCardPriority() throws Exception {

        log.info("board = {}", board);

        KanbanBoardCardSwitchRequest request = KanbanBoardCardSwitchRequest.builder()
                .teamId(team.getId())
                .cardId(card1.getId())
                .currentBoardId(board.getId())
                .newPriority(Math.toIntExact(2L))
                .newBoardId(board.getId())
                .build();

        //when
        //cardid currentBoardId, newPriority, new BoardId
        KanbanBoardAndCardResponse result = kanbanBoardCardService.changeCardPriority(request);
        log.info("result = {}", result);

        KanbanBoardResponse board1 = result.getKanbanBoards().get(0);

        assertNotNull(result, "카드가 not null 이여야 합니다.");
        assertEquals(board1.getCards().get(0).getId(), card1.getId(), "ID가 일치해야 합니다.");
        assertEquals(board1.getCards().get(0).getPriority(),2L, "priority가 일치해야 합니다.");


    }

    @Test
    @DisplayName("카드 삭제")
    void deleteKanbanBoardCard() throws Exception {

        // given
        Long priority = 1L;
        String content = "TestContent";

        KanbanBoardCard createdCard = kanbanBoardCardService.create(content, member, board, priority);

        //when
        KanbanBoardMessageResponse deletedBoardCard = kanbanBoardCardService.deleteBoardCard(createdCard.getId());
        String message = deletedBoardCard.getMessage();

        //then
        Assertions.assertThat(message).isEqualTo("Success Delete Card");
        log.info(" deleteCard 테스트 성공: {}", message);


    }
}
