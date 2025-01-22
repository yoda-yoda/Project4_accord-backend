package org.noteam.be.joinBoard.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.noteam.be.joinBoard.domain.JoinBoard;
import org.noteam.be.joinBoard.domain.Status;
import org.noteam.be.joinBoard.dto.JoinBoardCardResponse;
import org.noteam.be.joinBoard.dto.JoinBoardRegisterRequest;
import org.noteam.be.joinBoard.dto.JoinBoardResponse;
import org.noteam.be.joinBoard.dto.JoinBoardUpdateRequest;
import org.noteam.be.joinBoard.repository.JoinBoardRepository;
import org.noteam.be.system.exception.ExceptionMessage;
import org.noteam.be.system.exception.joinBoard.JoinBoardNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@Slf4j
class JoinBoardServiceImplTest {

    @Autowired
    private JoinBoardServiceImpl joinBoardService;
    @Autowired
    private JoinBoardRepository joinBoardRepository;


    // BeforeEach 엔티티를 저장해놓기위한 변수
    JoinBoardResponse joinBoardResponse;


    @BeforeEach
    void setUp1() {

        JoinBoardRegisterRequest title = JoinBoardRegisterRequest.builder()
                .title("title")
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


    @Test
    @DisplayName("createJoinBoardByDto 메서드 테스트1 - 유저의 입력값을 Request Dto로 받고, 해당 글을 저장하는 메서드")
    void createJoinBoardByDto_method_test1() throws Exception {
    		// given
        // BeforeEach로 인해 1개의 엔티티 생성

    	  // when
        JoinBoardRegisterRequest build = JoinBoardRegisterRequest.builder()
                .title("프론트엔트 2명 구합니다")
                .topic("인스타그램같은 웹페이지 만들기")
                .teamName("짱짱팀")
                .projectBio("이 프로젝트는 인스타그램과 비슷한 웹페이지를 만드는 프로젝트 입니다")
                .teamBio("백엔드 2명이 있고, 기술 스택은 자바 스프링 입니다")
                .content("리액트와 웹소켓을 사용합니다")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .peopleNumber(3)
                .build();

        JoinBoardResponse res = joinBoardService.createJoinBoardByDto(build);

        JoinBoard find1 = joinBoardRepository.findById(joinBoardResponse.getId()+1L)
                .orElseThrow(() -> new JoinBoardNotFoundException(ExceptionMessage.JoinBoard.JOIN_BOARD_NOT_FOUND_ERROR));

        // then
        assertThat(res.getId()).isEqualTo(joinBoardResponse.getId()+1L);
        assertThat(res.getContent()).isEqualTo(build.getContent());
        assertThat(find1.getStatus()).isEqualTo(Status.ACTIVE);

    }


    @Test
    @DisplayName("getJoinBoardEntityById 메서드 테스트1 - id로 JoinBoard 엔티티를 찾아오는 메서드이고, 찾지못할시 예외도 테스트 한다. ")
    void getJoinBoardEntityById_method_test1() throws Exception {
    		// given
        // BeforeEach로 인해 1개의 엔티티 생성

    	  // when
        JoinBoard get1 = joinBoardService.getJoinBoardEntityById(joinBoardResponse.getId());

        // then
        assertThat(get1.getId()).isEqualTo(joinBoardResponse.getId());

        // 설정해둔 예외대로 잘 발생하는지도 체크
        assertThatThrownBy
                (() -> joinBoardService.getJoinBoardEntityById(joinBoardResponse.getId()+1L))
                .isInstanceOf(JoinBoardNotFoundException.class)
                .hasMessage(ExceptionMessage.JoinBoard.JOIN_BOARD_NOT_FOUND_ERROR);

        // 결과: ok

    }

    @Test
    @DisplayName("getJoinBoardEntityByIdWithNoDeleted 메서드 테스트1 - id로 JoinBoard 엔티티를 찾아오는데 딜리트처리 안된것만 찾아오는 메서드")
    void getJoinBoardEntityByIdWithNoDeleted_method_test1() throws Exception {
    		// given
        // BeforeEach로 인해 1개의 엔티티 생성

        // when
        JoinBoard get1 = joinBoardService.getJoinBoardEntityByIdWithNoDeleted(joinBoardResponse.getId());

        // then
        assertThat(get1.getId()).isEqualTo(joinBoardResponse.getId());
        log.info("get1.getContent() = {}", get1.getContent());

    }


    @Test
    @DisplayName("getJoinBoardEntityByIdWithNoDeleted 메서드 테스트2 - id로 JoinBoard 엔티티를 찾아오는데 딜리트처리 안된것만 찾아오는 메서드 -> delete 된걸 찾으려할때 예외 잘 발생하는지 테스트")
    void getJoinBoardEntityByIdWithNoDeleted_method_test2() throws Exception {
        // given
        // BeforeEach로 인해 1개의 엔티티 생성

        JoinBoard find1 = joinBoardRepository.findById(joinBoardResponse.getId())
                .orElseThrow(() -> new JoinBoardNotFoundException(ExceptionMessage.JoinBoard.JOIN_BOARD_NOT_FOUND_ERROR));

        // when
        find1.changeStatus(Status.DELETE);
        joinBoardRepository.save(find1);

        // then
        assertThatThrownBy
                ( () -> joinBoardService.getJoinBoardEntityByIdWithNoDeleted(joinBoardResponse.getId()) )
                .isInstanceOf(JoinBoardNotFoundException.class)
                .hasMessage(ExceptionMessage.JoinBoard.JOIN_BOARD_NOT_FOUND_ERROR);

    }

    @Test
    @DisplayName("getJoinBoardById 메서드 테스트1 - id로 JoinBoard 엔티티를 delete 아닌것만 찾고 응답dto로 반환하는 메서드")
    void getJoinBoardById_method_test1() throws Exception {
    		// given
        // BeforeEach로 인해 1개의 엔티티 생성

    	  // when
        JoinBoardResponse res = joinBoardService.getJoinBoardById(joinBoardResponse.getId());

        // then
        assertThat(res.getId()).isEqualTo(joinBoardResponse.getId());
        assertThat(res.getContent()).isEqualTo(joinBoardResponse.getContent());

    }


    @Test
    @DisplayName("getJoinBoardById 메서드 테스트2 - id로 JoinBoard 엔티티를 delete 아닌것만 찾고 응답dto로 반환하는 메서드 -> delete 된걸 찾으려할때 예외 잘 발생하는지 테스트")
    void getJoinBoardById_method_test2() throws Exception {

        // given
        // BeforeEach로 인해 1개의 엔티티 생성

        JoinBoard find1 = joinBoardRepository.findById(joinBoardResponse.getId())
                .orElseThrow(() -> new JoinBoardNotFoundException(ExceptionMessage.JoinBoard.JOIN_BOARD_NOT_FOUND_ERROR));

        // when
        find1.changeStatus(Status.DELETE);
        joinBoardRepository.save(find1);

        // then
        assertThatThrownBy
                ( () -> joinBoardService.getJoinBoardById(joinBoardResponse.getId()) )
                .isInstanceOf(JoinBoardNotFoundException.class)
                .hasMessage(ExceptionMessage.JoinBoard.JOIN_BOARD_NOT_FOUND_ERROR);

    }

    @Test
    @DisplayName("getAllJoinBoard 메서드 테스트1 -  DB에서 글을 전부 찾아 JoinBoardResponse 로 변환후 리스트에 담아 반환하는 메서드 -> DELETE 필터링까지 잘 되는지 체크중")
    void getAllJoinBoard_method_test1() throws Exception {
    		// given
        // BeforeEach로 인해 1개의 엔티티 생성된 상태고, 하나더 생성후 delete 처리.
        JoinBoard build = JoinBoard.builder()
                .title("프론트엔트 2명 구합니다")
                .topic("인스타그램같은 웹페이지 만들기")
                .teamName("짱짱팀")
                .projectBio("이 프로젝트는 인스타그램과 비슷한 웹페이지를 만드는 프로젝트 입니다")
                .teamBio("백엔드 2명이 있고, 기술 스택은 자바 스프링 입니다")
                .content("리액트와 웹소켓을 사용합니다")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .peopleNumber(2)
                .build();

        JoinBoard saved = joinBoardRepository.save(build);

        saved.changeStatus(Status.DELETE);

        joinBoardRepository.save(saved);


        // when
        List<JoinBoardResponse> list = joinBoardService.getAllJoinBoard();

        // then
        assertThat(list.size()).isEqualTo(1);

        JoinBoardResponse res = list.get(0);
        assertThat(res.getId()).isEqualTo(joinBoardResponse.getId());

    }


    @Test
    @DisplayName("getAllJoinBoard 메서드 테스트2 - 해당하는것이 없을때 빈 리스트를 잘 반환하는지 확인하는 메서드")
    void getAllJoinBoard_method_test2() throws Exception {
        // given
        // BeforeEach로 인해 1개의 엔티티 생성된 상태고, DB를 아예 지울것임.
        joinBoardRepository.deleteAll();

        // when
        List<JoinBoardResponse> list = joinBoardService.getAllJoinBoard();

        // then
        assertThat(list.size()).isEqualTo(0);
        log.info("list.toString() = {}", list.toString()); // []

    }


    @Test
    @DisplayName("getAllJoinBoardCard 메서드 테스트1 - DB에서 글을 전부 찾아 카드용 응답으로 변환후 리스트에 담아 반환하는 메서드 -> DELETE 필터링까지 잘 되는지 체크중")
    void getAllJoinBoardCard_method_test1() throws Exception {
        // given
        // BeforeEach로 인해 1개의 엔티티 생성된 상태고, 하나더 생성후 delete 처리.

        JoinBoard build = JoinBoard.builder()
                .title("프론트엔트 2명 구합니다")
                .topic("인스타그램같은 웹페이지 만들기")
                .teamName("짱짱팀")
                .projectBio("이 프로젝트는 인스타그램과 비슷한 웹페이지를 만드는 프로젝트 입니다")
                .teamBio("백엔드 2명이 있고, 기술 스택은 자바 스프링 입니다")
                .content("리액트와 웹소켓을 사용합니다")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .peopleNumber(2)
                .build();

        JoinBoard saved = joinBoardRepository.save(build);

        saved.changeStatus(Status.DELETE);
        joinBoardRepository.save(saved);


        // when
        List<JoinBoardCardResponse> list = joinBoardService.getAllJoinBoardCard();

        // then
        assertThat(list.size()).isEqualTo(1);

        JoinBoardCardResponse res = list.get(0);
        assertThat(res.getId()).isEqualTo(joinBoardResponse.getId());

    }



    @Test
    @DisplayName("getAllJoinBoardCard 메서드 테스트2 - 해당하는것이 없을때 빈 리스트를 잘 반환하는지 확인하는 메서드")
    void getAllJoinBoardCard_method_test2() throws Exception {
        // given
        // BeforeEach로 인해 1개의 엔티티 생성된 상태고, DB를 아예 지울것임.
        joinBoardRepository.deleteAll();

        // when
        List<JoinBoardCardResponse> list = joinBoardService.getAllJoinBoardCard();

        // then
        assertThat(list.size()).isEqualTo(0);
        log.info("list.toString() = {}", list.toString()); // []
    }




    @Test
    @DisplayName("updateJoinBoardById 메서드 테스트1 - 기존 엔티티값을 수정하는 메서드")
    void updateJoinBoardById_method_test1() throws Exception {

        // given
        // BeforeEach로 인해 1개의 엔티티 생성


        // when
        JoinBoardUpdateRequest build = JoinBoardUpdateRequest.builder()
                .title("t")
                .topic("t")
                .teamName("t")
                .projectBio("t")
                .teamBio("t")
                .content("t")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .peopleNumber(1)
                .build();

        JoinBoardResponse res = joinBoardService.updateJoinBoardById(joinBoardResponse.getId(), build);


        // then
        assertThat(res.getId()).isEqualTo(joinBoardResponse.getId());
        assertThat(res.getContent()).isEqualTo(build.getContent());

        log.info("res.getTitle() = {}", res.getTitle());
        log.info("res.getTopic() = {}", res.getTopic());

        // 결과: ok

    }


    
    @Test
    @DisplayName("deleteJoinBoardById 메서드 테스트1 - 기존 엔티티를 softDelete 처리하는 메서드 (즉 Enum타입의 status를 ACTIVE에서 DELETE로 처리) ")
    void deleteJoinBoardById_method_test1() throws Exception {

    		// given
        // BeforeEach로 엔티티 1개 생성
        
    	  // when
        JoinBoard find1 = joinBoardRepository.findById(joinBoardResponse.getId())
                .orElseThrow(() -> new JoinBoardNotFoundException(ExceptionMessage.JoinBoard.JOIN_BOARD_NOT_FOUND_ERROR));

        // 기존 status는 ACTIVE 인지 확인
        assertThat(find1.getStatus()).isEqualTo(Status.ACTIVE);
        log.info("find1.getStatus() = {}", find1.getStatus());

        joinBoardService.deleteJoinBoardById(joinBoardResponse.getId());

        JoinBoard find2 = joinBoardRepository.findById(joinBoardResponse.getId())
                .orElseThrow(() -> new JoinBoardNotFoundException(ExceptionMessage.JoinBoard.JOIN_BOARD_NOT_FOUND_ERROR));

        // then
        assertThat(find2.getStatus()).isEqualTo(Status.DELETE);
        log.info("find2.getStatus() = {}", find2.getStatus());

        // 결과: ok
    }

}