package org.noteam.be.kanbanBoard.service.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.kanbanBoard.domain.KanbanBoard;
import org.noteam.be.kanbanBoard.domain.KanbanBoardCard;
import org.noteam.be.kanbanBoard.dto.KanbanBoardMessageResponse;
import org.noteam.be.kanbanBoard.repository.KanbanBoardCardRepository;
import org.noteam.be.kanbanBoard.service.KanbanBoardCardService;
import org.noteam.be.kanbanBoard.service.KanbanBoardService;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.service.MemberService;
import org.noteam.be.system.exception.ExceptionMessage;
import org.noteam.be.system.exception.kanbanboard.KanbanboardCardNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class KanbanBoardCardServiceImpl implements KanbanBoardCardService {

    private final KanbanBoardCardRepository kanbanBoardCardRepository;
    private final MemberService memberService;
    private final KanbanBoardService kanbanBoardService;
    private final EntityManager entityManager;

    @Override
    public KanbanBoardCard create(String content, Member member, KanbanBoard board, Long num) {

        KanbanBoardCard boardCard = KanbanBoardCard.builder()
                .content(content)
                .member(member)
                .board(board)
                .priority(num)
                .build();
         return kanbanBoardCardRepository.save(boardCard);

    }

    //Card 조회 로직
    @Override
    public KanbanBoardCard getKanbanBoardCard(Long id) {
        KanbanBoardCard kanbanBoardCard = kanbanBoardCardRepository.findById(id)
                .orElseThrow(() -> new KanbanboardCardNotFoundException(ExceptionMessage.Kanbanboard.KANBANBOARD_CARD_NOT_FOUND_ERROR));
        return kanbanBoardCard;
    }

    @Override
    public List<KanbanBoardCard> getKanbanBoardCardbyBoardId(Long boardId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "priority");
        List<KanbanBoardCard> byBoardId = kanbanBoardCardRepository.findByBoardId(boardId, sort);

        return byBoardId;
    }

    //Card 추가 로직
    @Override
    public KanbanBoardMessageResponse createCard(Long memberId, Long teamId ,String title, String content) {

        int num = 0;

        // id로 멤버 조회 이 부분 서큐리티 컨피그로 수정...
        Member member = memberService.getByMemberId(memberId);
        log.info("member = {}", member);


        //해당 보드 조회
        KanbanBoard byTeamIdAndBoardName = kanbanBoardService.getKanbanBoardbyTeamIdAndTitle(teamId, title);
        log.info("byTeamIdAndBoardName = {}", byTeamIdAndBoardName);

        if (byTeamIdAndBoardName.getCards() == null) {
            num = 1;
        }

        if (byTeamIdAndBoardName.getCards() != null) {
            num = byTeamIdAndBoardName.getCards().size()+1;
        }

        create(content,member,byTeamIdAndBoardName, (long) num);

        //보드 카드 추가
        return KanbanBoardMessageResponse.builder()
                .message("Success Create Card")
                .result(true)
                .build();
    }

    @Override
    public KanbanBoardMessageResponse deleteBoardCard(Long cardId) {

        KanbanBoardCard kanbanBoardCard = getKanbanBoardCard(cardId);

        Long boardId = kanbanBoardCard.getBoard().getId();
        log.info("id = {}", boardId);

        //지우는거
        kanbanBoardCardRepository.deleteById(cardId);

        // 보드 팀 기준 가져오고 정렬 priority
        Sort sort = Sort.by(Sort.Direction.ASC, "priority");
        List<KanbanBoardCard> sortedKanbanBoardCardIDList = kanbanBoardCardRepository.findByBoardId(boardId,sort);


        System.out.println("for 문 도착전");

        Long i = 1L;

        for(KanbanBoardCard card : sortedKanbanBoardCardIDList) {
            card.setPriority(i++);
        }


        return KanbanBoardMessageResponse.builder()
                .message("Success Delete Card")
                .result(true)
                .build();
    }

    @Override
    public KanbanBoardMessageResponse updateCard(Long id, String content) {

        KanbanBoardCard kanbanBoardCard = kanbanBoardCardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("KanbanBoard not found with id: " + id));

        // 더티 체킹 적용: 엔티티의 값만 변경
        kanbanBoardCard.setContent(content);

        return  KanbanBoardMessageResponse.builder()
                .message("Sucess Card Update")
                .result(true)
                .build();

    }

    @Override
    public KanbanBoardMessageResponse changeCardPriority(Long cardId, Long boardId, int dropSpotNum) {

        // 들어갈 카드
        KanbanBoardCard card = getKanbanBoardCard(cardId);
        log.info("card = {}", card);

        // 가져올 카드 리스트
        List<KanbanBoardCard> cardList = getKanbanBoardCardbyBoardId(boardId);
        log.info("cardList = {}", cardList);

        // 가져온 보드 정보
        KanbanBoard board = cardList.getFirst().getBoard();
        log.info("board = {}", board);

        // 2 10
        System.out.println("for문 전");

        if (card.getPriority() < dropSpotNum) {
            System.out.println("card.getPriority() <<<<<<< dropSpotNum");
            //3
            for (int i = dropSpotNum + 1; i <= cardList.size(); i++) {

                System.out.println("for문 도착");
                //get 2
                KanbanBoardCard kanbanBoardCard = cardList.get(i - 1);
                //set 3
                kanbanBoardCard.setPriority((long) (i + 1));

                log.info("가져온 인덱스 num = {}", i - 1);
                log.info("기존 get = {}", i);
                log.info("이후 set = {}", i + 1);

            }

            card.setBoard(board);

            log.info("set dropSpotNum = {}", dropSpotNum);

            card.setPriority((long) dropSpotNum);
            Long priority = card.getPriority();
            log.info("priority = {}", priority);

        }

        if (card.getPriority() > dropSpotNum) {
            System.out.println("card.getPriority() >>>>>>>>>>> dropSpotNum");
            for (int i = dropSpotNum; i <= cardList.size(); i++) {

                System.out.println("for문 도착");
                //get 2
                KanbanBoardCard kanbanBoardCard = cardList.get(i - 1);
                //set 3
                kanbanBoardCard.setPriority((long) (i + 1));

                log.info("가져온 인덱스 num = {}", i - 1);
                log.info("기존 get = {}", i);
                log.info("이후 set = {}", i + 1);

            }

            card.setBoard(board);

            log.info("set dropSpotNum = {}", dropSpotNum);

            card.setPriority((long) dropSpotNum);
            Long priority = card.getPriority();
            log.info("priority = {}", priority);

        }

        return  KanbanBoardMessageResponse.builder()
                .message("Sucess Card Position Change")
                .result(true)
                .build();
    }

    @Override
    public String forEachCard(List<KanbanBoardCard> cardList) {

        Long i = 1L;

        for(KanbanBoardCard cards : cardList) {
            cards.setPriority(i++);
            log.info("foreach문 cards.getPriority() = {}", cards.getPriority());
        }

        return "forEach문 완료";
    }



}

