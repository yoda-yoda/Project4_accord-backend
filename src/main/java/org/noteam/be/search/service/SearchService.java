package org.noteam.be.search.service;

import org.noteam.be.joinBoard.dto.JoinBoardCardResponse;
import org.noteam.be.search.dto.SearchRequest;
import org.springframework.data.domain.Page;

public interface SearchService {

    Page<JoinBoardCardResponse> getSearchJoinBoardCardByPage(int page, SearchRequest req);
}
