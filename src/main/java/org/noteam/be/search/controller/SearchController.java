package org.noteam.be.search.controller;

import lombok.RequiredArgsConstructor;
import org.noteam.be.joinBoard.dto.JoinBoardCardResponse;
import org.noteam.be.search.dto.SearchRequest;
import org.noteam.be.search.service.SearchService;
import org.noteam.be.system.response.ResponseCode;
import org.noteam.be.system.response.ResponseData;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/join-board")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;


    // 메서드 기능: 유저의 검색값이, 제목이나 주제에 포함되는 데이터를 찾는다.
    // 그리고 페이지 설정값을 매개변수로 받아 page 객체를 반환한다. 글 정렬은 최신순으로 이뤄진다.
    // 예외: 없다. 즉 DB에 해당 값이 없다면, 빈 페이지 객체를 반환한다.
    // 반환: 해당 엔티티를 전부 JoinBoardCardResponse 라는 dto로 변환하고, 그것을 page 객체로 만들어 반환한다.
    @PostMapping("/search")
    public ResponseEntity<ResponseData<Page<JoinBoardCardResponse>>> getSearchPagedList(
            @RequestParam(value = "page", defaultValue = "0") int page, @RequestBody SearchRequest dto)
    {
        Page<JoinBoardCardResponse> pagedList = searchService.getSearchJoinBoardCardByPage(page, dto);

        return ResponseData.toResponseEntity(ResponseCode.SEARCH_JOIN_BOARD_SUCCESS, pagedList);
    }


}
