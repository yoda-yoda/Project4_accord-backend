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
    JoinBoardResponse joinBoardResponse;


    @BeforeEach
    void setUp1() {

        JoinBoardRegisterRequest title = JoinBoardRegisterRequest.builder()
                .title("프론트엔드 같이 배우면서 하실분 구합니다.")
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
        log.info("==========BeforeEach 끝==========");

    }

    @AfterEach
    void tearDown() {
        joinBoardRepository.deleteAll();
    }

    // 다음에 다시 테스트해보기
    @Test
    @DisplayName("getSearchJoinBoardCardByPage 메서드 테스트1 - '프론' 이라는 단어 검색시 해당 데이터를 잘 찾아오는지 테스트")
    void getSearchJoinBoardCardByPage_method_test1() throws Exception {
    		// given
            // BeforeEach로 엔티티 1개 추가

        SearchRequest req = new SearchRequest();
        req.setInput("프론");

        // when
        Page<JoinBoardCardResponse> res1 = searchService.getSearchJoinBoardCardByPage(0, req);

        // then
        for (JoinBoardCardResponse res2 : res1.getContent()) {
            assertThat(res2.getTitle()).isEqualTo(joinBoardResponse.getTitle());
            log.info("res2.getTitle() = {}", res2.getTitle());
        }

    }

}