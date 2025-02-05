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
        KanbanBoard byTeamIdAndBoardName = kanbanBoardService.getKanbanBoardByTeamIdAndBoardId(KanbanBoardSecondLookupRequest
                .builder()
                .teamId(request.getTeamId())
                .boardId(request.getColumnId())
                .build());

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
    public KanbanBoardAndCardResponse changeCardPriority(KanbanBoardCardSwitchRequest request) {
        // 타겟 카드를 가져온다.
        KanbanBoardCard targetCard = getKanbanBoardCard(request.getCardId());
        // 기존 보드를 가져온다
        KanbanBoard currentBoard = kanbanBoardService.getKanbanBoardById(request.getCurrentBoardId() );
        KanbanBoard newBoard = kanbanBoardService.getKanbanBoardById(request.getNewBoardId());
        // 새로운 보드를 가져온다.
        // 새로운 보드의 카드를 가져온다.
        List<KanbanBoardCard> newBoardCardList = getKanbanBoardCardbyBoardId(request.getNewBoardId());
        // 프리오리티가 1보다 작다면 거나 새로운 카드리스트의 사이즈 보다 작다면 에러를 던진다.
        if (request.getNewPriority() < 1 || request.getNewPriority() > newBoardCardList.size() + 1) {
            throw new OutOfRangeKanbanBoardException(ExceptionMessage.Kanbanboard.KANBANBOARD_OUT_OF_RANGE_ERROR);
        }
        // 기존 보드가 와 새로운 보드가 같은지 비교를 한다.
        boolean isSameBoard = request.getCurrentBoardId().equals(request.getNewBoardId());

        // changePriorityWitheSameBoard라는 코드를 실행한다.
        if (isSameBoard) {
            return changePriorityWithinSameBoard(targetCard, currentBoard, request.getNewPriority(), request.getTeamId());
        }
        return moveCardToAnotherBoard(targetCard, currentBoard, newBoard, request.getNewPriority(), request.getTeamId());
    }

    @Transactional
    protected KanbanBoardAndCardResponse changePriorityWithinSameBoard(KanbanBoardCard targetCard, KanbanBoard board, int newPriority, Long teamId) {

        //보드 정보를 가져온다.
        List<KanbanBoardCard> cardList = getKanbanBoardCardbyBoardId(board.getId());

        //타겟 카드의 priority를 가져온다. int로.
        int currentPriority = targetCard.getPriority().intValue();

        //타겟 카드와 새로운 카드의 프리오리티가 같다면 그냥 기존 보드를 내려준다.
        if (currentPriority == newPriority) {
            return kanbanBoardService.findByTeamId(teamId);
        }
        //cardList의 카드를 foreach문을 돌린다.
        for (KanbanBoardCard card : cardList) {
            // 카드의 priority를 가져온다.
            int priority = card.getPriority().intValue();

            // 기존대상 카드의 priority가 새로운 drop할 장소 priority보다 작다면
            if (currentPriority < newPriority) {
                // 반복문의 priority가 기존대상의 priority 보다 작고 , 새로운 priority보다 같거나 작다면.
                // 반복문의 priority 보다 -1을 하여 저장한다.
                if (priority > currentPriority && priority <= newPriority) {
                    card.setPriority((long) (priority - 1));
                }
            } else {
                // 기존대상 카드의 priority가 새로운 drop할 장소 priority랑 같거나 크다면
                // 반복문의 priority가 새로운 drop할 장소 priority보다 크거나 같고, 기존의 priority보다 작으면
                // 반복문의 priority 보다 +1을 하여 저장한다.
                if (priority >= newPriority && priority < currentPriority) {
                    card.setPriority((long) (priority + 1));
                }
            }
        }
        // 그리고 targetCard의 priority를 바꿔준다.
        targetCard.setPriority((long) newPriority);

        // 반복한 카드 리스트를 저장해준다.
        kanbanBoardCardRepository.saveAll(cardList);
        // targetCard를 저장해준다.
        kanbanBoardCardRepository.save(targetCard);
        // flush를 해야지 return에서 반영된 데이터를 가져 갈 수 있다.
        kanbanBoardCardRepository.flush();

        return kanbanBoardService.findByTeamId(teamId);

    }

    @Transactional
    protected KanbanBoardAndCardResponse moveCardToAnotherBoard(KanbanBoardCard targetCard, KanbanBoard currentBoard, KanbanBoard newBoard, int newPriority, Long teamId) {
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

        kanbanBoardCardRepository.flush();

        return kanbanBoardService.findByTeamId(teamId);
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

