package org.noteam.be.search.service.Impl;

import lombok.RequiredArgsConstructor;
import org.noteam.be.joinBoard.domain.JoinBoard;
import org.noteam.be.joinBoard.domain.Status;
import org.noteam.be.joinBoard.dto.JoinBoardCardResponse;
import org.noteam.be.search.dto.SearchRequest;
import org.noteam.be.search.repository.SearchRepository;
import org.noteam.be.search.service.SearchService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {


    private final SearchRepository searchRepository;



    // 메서드 기능: 유저의 검색값이, 제목이나 주제에 포함되는 데이터를 찾는다.
    // 그리고 페이지 설정값을 매개변수로 받아 page 객체를 반환한다. 글 정렬은 최신순으로 이뤄진다.
    // 예외: 없다. 즉 DB에 해당 값이 없다면, 빈 페이지 객체를 반환한다.
    // 반환: 해당 엔티티를 전부 JoinBoardCardResponse 라는 dto로 변환하고, 그것을 page 객체로 만들어 반환한다.
    public Page<JoinBoardCardResponse> getSearchJoinBoardCardByPage(int page, SearchRequest req) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdAt"));

        // 페이지 설정값(설정 객체) 생성
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        // 검색값, 페이지 설정값, ACTIVE 상태를 매개변수로 전달하며 해당되는 글을 찾아 Page 객체를 생성
        Page<JoinBoard> pagingEntity = searchRepository.findByTitleContainingAndStatusOrTopicContainingAndStatus(req.getInput(), Status.ACTIVE, req.getInput(), Status.ACTIVE, pageable);

        // 나중에 페이지 객체로 바꿀때 사용할 List 변수
        List<JoinBoardCardResponse> cardList = new ArrayList<>();

        // 만약 DB에 해당 값이 없다면, 비어있는 페이지 객체를 만들어서 반환
        if (  pagingEntity.getContent().isEmpty() ) {
            Page<JoinBoardCardResponse> emptyPage = new PageImpl<>(cardList, pageable, pagingEntity.getTotalElements());
            return emptyPage;
        }

        // 값이 존재한다면, 페이지 객체 내부에 있는 각각의 엔티티를 모두 Card dto로 변환하여 List 변수에 담는다
        for (JoinBoard joinBoard : pagingEntity.getContent()) {
            JoinBoardCardResponse res = JoinBoardCardResponse.getResponseFromEntity(joinBoard);
            cardList.add(res);
        }

        // PageImpl 객체의 생성자를 통해, 최종 Page 객체를 만들었다
        Page<JoinBoardCardResponse> pagingCard = new PageImpl<>(cardList, pageable, pagingEntity.getTotalElements());

        // 최종 Page 객체 반환
        return pagingCard;
    }




}
