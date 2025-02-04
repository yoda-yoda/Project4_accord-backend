package org.noteam.be.search.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.noteam.be.joinBoard.dto.JoinBoardCardResponse;
import org.noteam.be.joinBoard.dto.JoinBoardRegisterRequest;
import org.noteam.be.joinBoard.dto.JoinBoardResponse;
import org.noteam.be.joinBoard.repository.JoinBoardRepository;
import org.noteam.be.joinBoard.service.Impl.JoinBoardServiceImpl;
import org.noteam.be.search.dto.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Slf4j
class SearchServiceImplTest {

    @Autowired
    private JoinBoardServiceImpl joinBoardService;
    @Autowired
    private SearchServiceImpl searchService;
    @Autowired
    private JoinBoardRepository joinBoardRepository;


    // BeforeEach 엔티티를 저장해놓기위한 변수
    List<JoinBoardResponse> joinBoardResponseList = new ArrayList<>();


    @BeforeEach
    void setUp1() {

        JoinBoardRegisterRequest req1 = JoinBoardRegisterRequest.builder()
                .title("테스트 테스트1")
                .topic("topic")
                .teamName("teamName")
                .projectBio("projectBio")
                .teamBio("teamBio")
                .content("content")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .peopleNumber(3)
                .build();


        JoinBoardRegisterRequest req2 = JoinBoardRegisterRequest.builder()
                .title("title")
                .topic("테스트 테스트2")
                .teamName("teamName")
                .projectBio("projectBio")
                .teamBio("teamBio")
                .content("content")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .peopleNumber(3)
                .build();


        JoinBoardRegisterRequest req3 = JoinBoardRegisterRequest.builder()
                .title("테스트 테스트3")
                .topic("topic")
                .teamName("teamName")
                .projectBio("projectBio")
                .teamBio("teamBio")
                .content("content")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .peopleNumber(3)
                .build();


        JoinBoardResponse joinBoardRes1 = joinBoardService.createJoinBoardByDto(req1);
        JoinBoardResponse joinBoardRes2 = joinBoardService.createJoinBoardByDto(req2);
        JoinBoardResponse joinBoardRes3 = joinBoardService.createJoinBoardByDto(req3);

        joinBoardService.deleteJoinBoardById(joinBoardRes3.getId());

        log.info("==========BeforeEach 끝==========");

    }

    @AfterEach
    void tearDown() {
        joinBoardRepository.deleteAll();
    }


    @Test
    @DisplayName("getSearchJoinBoardCardByPage 메서드 테스트1 - BeforeEach 작동시킨뒤 상황에서 '테스트' 라고 검색시 해당 데이터 잘 가져오는지 확인하는 메서드")
    void getSearchJoinBoardCardByPage_method_test1() throws Exception {

    		// given
        // BoforeEach 작동 => 즉 "테스트" 검색이 가능한 엔티티를 3개 저장후 하나는 delete 처리함
        
        SearchRequest req = new SearchRequest();
        req.setInput("테스트");
        
        // when
        Page<JoinBoardCardResponse> res = searchService.getSearchJoinBoardCardByPage(0, req);

        // then
        assertThat(res.getContent().size()).isEqualTo(2);
        log.info("res.getContent().size() = {}", res.getContent().size());

    }




}