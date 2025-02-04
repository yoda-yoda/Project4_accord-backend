package org.noteam.be.kanbanBoard.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.kanbanBoard.domain.KanbanBoard;
import org.noteam.be.kanbanBoard.domain.KanbanBoardCard;
import org.noteam.be.kanbanBoard.dto.*;
import org.noteam.be.kanbanBoard.repository.KanbanBoardCardRepository;
import org.noteam.be.kanbanBoard.service.KanbanBoardCardService;
import org.noteam.be.kanbanBoard.service.KanbanBoardService;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.service.MemberService;
import org.noteam.be.system.exception.ExceptionMessage;
import org.noteam.be.system.exception.kanbanboard.KanbanboardCardNotFoundException;
import org.noteam.be.system.exception.kanbanboard.OutOfRangeKanbanBoardException;
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
    public KanbanBoardMessageResponse createCard(KanbanBoardCardCreateRequest request) {

        int num = 0;

        // id로 멤버 조회 이 부분 서큐리티 컨피그로 수정...
        Member member = memberService.getByMemberId(request.getMemberId());
        log.info("member = {}", member);

        //해당 보드 조회
        KanbanBoard byTeamIdAndBoardName = kanbanBoardService.getKanbanBoardbyTeamIdAndTitle(KanbanBoardLookupRequest.builder()
                .teamId(request.getTeamId()).title(request.getTitle()).build());
        log.info("byTeamIdAndBoardName = {}", byTeamIdAndBoardName);

        if (byTeamIdAndBoardName.getCards() == null) {
            num = 1;
        }

        if (byTeamIdAndBoardName.getCards() != null) {
            num = byTeamIdAndBoardName.getCards().size()+1;
        }

        create(request.getContent(),member,byTeamIdAndBoardName, (long) num);

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
    public KanbanBoardMessageResponse updateCard(KanbanBoardCardUpdateRequest request) {

        KanbanBoardCard kanbanBoardCard = kanbanBoardCardRepository.findById(request.getCardId())
                .orElseThrow(() -> new IllegalArgumentException("KanbanBoard not found with id: " + request.getCardId()));

        // 더티 체킹 적용: 엔티티의 값만 변경
        kanbanBoardCard.setContent(request.getContent());

        return  KanbanBoardMessageResponse.builder()
                .message("Sucess Card Update")
                .result(true)
                .build();

    }

    @Transactional
    @Override
    public KanbanBoardMessageResponse changeCardPriority(KanbanBoardCardSwitchRequest request) {
        KanbanBoardCard targetCard = getKanbanBoardCard(request.getCardId());

        KanbanBoard currentBoard = kanbanBoardService.getKanbanBoardById(request.getCurrentBoardId() );
        KanbanBoard newBoard = kanbanBoardService.getKanbanBoardById(request.getNewBoardId());

        List<KanbanBoardCard> newBoardCardList = getKanbanBoardCardbyBoardId(request.getNewBoardId());

        if (request.getNewPriority() < 1 || request.getNewPriority() > newBoardCardList.size() + 1) {
            throw new OutOfRangeKanbanBoardException(ExceptionMessage.Kanbanboard.KANBANBOARD_OUT_OF_RANGE_ERROR);
        }

        boolean isSameBoard = request.getCurrentBoardId().equals(request.getNewBoardId());

        if (isSameBoard) {
            return changePriorityWithinSameBoard(targetCard, currentBoard, Math.toIntExact(request.getNewBoardId()));
        }
        return moveCardToAnotherBoard(targetCard, currentBoard, newBoard, Math.toIntExact(request.getNewBoardId()));
    }

    @Transactional
    protected KanbanBoardMessageResponse changePriorityWithinSameBoard(KanbanBoardCard targetCard, KanbanBoard board, int newPriority) {
        List<KanbanBoardCard> cardList = getKanbanBoardCardbyBoardId(board.getId());

        int currentPriority = targetCard.getPriority().intValue();
        if (currentPriority == newPriority) {
            return KanbanBoardMessageResponse.builder()
                    .message("No change in card priority")
                    .result(true)
                    .build();
        }

        for (KanbanBoardCard card : cardList) {
            int priority = card.getPriority().intValue();
            if (currentPriority < newPriority) {
                if (priority > currentPriority && priority <= newPriority) {
                    card.setPriority((long) (priority - 1));
                }
            } else {
                if (priority >= newPriority && priority < currentPriority) {
                    card.setPriority((long) (priority + 1));
                }
            }
        }

        targetCard.setPriority((long) newPriority);

        kanbanBoardCardRepository.saveAll(cardList);
        kanbanBoardCardRepository.save(targetCard);

        return KanbanBoardMessageResponse.builder()
                .message("Successfully changed card priority within the same board")
                .result(true)
                .build();
    }

    @Transactional
    protected KanbanBoardMessageResponse moveCardToAnotherBoard(KanbanBoardCard targetCard, KanbanBoard currentBoard, KanbanBoard newBoard, int newPriority) {
        List<KanbanBoardCard> currentBoardCardList = getKanbanBoardCardbyBoardId(currentBoard.getId());

        currentBoardCardList.remove(targetCard);
        for (KanbanBoardCard card : currentBoardCardList) {
            int priority = card.getPriority().intValue();
            if (priority > targetCard.getPriority()) {
                card.setPriority((long) (priority - 1));
            }
        }
        kanbanBoardCardRepository.saveAll(currentBoardCardList);

        List<KanbanBoardCard> newBoardCardList = getKanbanBoardCardbyBoardId(newBoard.getId());

        for (KanbanBoardCard card : newBoardCardList) {
            int priority = card.getPriority().intValue();
            if (priority >= newPriority) {
                card.setPriority((long) (priority + 1));
            }
        }

        targetCard.setBoard(newBoard);
        targetCard.setPriority((long) newPriority);
        newBoardCardList.add(targetCard);

        kanbanBoardCardRepository.saveAll(newBoardCardList);
        kanbanBoardCardRepository.save(targetCard);

        return KanbanBoardMessageResponse.builder()
                .message("Successfully moved card to another board and changed priority")
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

