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
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.Role;
import org.noteam.be.member.repository.MemberRepository;
import org.noteam.be.member.service.MemberService;
import org.noteam.be.profileimg.entity.ProfileImg;
import org.noteam.be.profileimg.repository.ProfileImgRepository;
import org.noteam.be.system.exception.ExceptionMessage;
import org.noteam.be.system.exception.joinBoard.JoinBoardNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.util.ArrayList;
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
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProfileImgRepository profileImgRepository;



    // BeforeEach 내부에서 생성한 엔티티를 저장해놓고, 테스트에 사용하기위한 변수들
    List<JoinBoardResponse> joinBoardResponseList = new ArrayList<>();
    List<Member> memberList = new ArrayList<>();


    // 이메일을 랜덤 생성해주는 메서드
    public static String createRandomEmail() {
        int randomNumber = (int) (Math.random() * 100000);
        String stringNumber = String.valueOf(randomNumber);
        return stringNumber + "@google.com";
    }


    // 닉네임을 스트링 숫자로 랜덤 생성해주는 메서드
    public static String createRandomNickname() {
        int randomNumber = (int) (Math.random() * 100000);
        String stringNumber = String.valueOf(randomNumber);
        return stringNumber;
    }


    @BeforeEach
    @DisplayName("member 2명 저장")
    void setUp1() {

        Member mem1 = Member.of(createRandomEmail(),
                createRandomNickname(),
                Role.MEMBER,
                org.noteam.be.member.domain.Status.ACTIVE,
                "provider");

        Member mem2 = Member.of(createRandomEmail(),
                createRandomNickname(),
                Role.MEMBER,
                org.noteam.be.member.domain.Status.ACTIVE,
                "provider");

        ProfileImg profile1 = ProfileImg.builder()
                .imageUrl("https://www.cheonyu.com/_DATA/product/63900/63992_1672648178.jpg")
                .build();

        ProfileImg profile2 = ProfileImg.builder()
                .imageUrl("https://www.cheonyu.com/_DATA/product/63900/63992_1672648178.jpg")
                .build();

        Member savedMem1 = memberRepository.save(mem1);
        Member savedMem2 = memberRepository.save(mem2);

        memberList.add(savedMem1);
        memberList.add(savedMem2);

        profile1.setMember(savedMem1);
        profile2.setMember(savedMem2);

        profileImgRepository.save(profile1);
        profileImgRepository.save(profile2);

        log.info("==========BeforeEach 1 끝==========");

    }


    @BeforeEach
    @DisplayName("각 멤버 1명당 joinBoard 2개씩, 총 4개의 글 저장")
    void setUp2() {

        JoinBoardRegisterRequest req1 = JoinBoardRegisterRequest.builder()
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

        JoinBoardRegisterRequest req2 = JoinBoardRegisterRequest.builder()
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

        JoinBoardRegisterRequest req3 = JoinBoardRegisterRequest.builder()
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

        JoinBoardRegisterRequest req4 = JoinBoardRegisterRequest.builder()
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

        joinBoardResponseList.add(joinBoardService.createJoinBoardByDto(req1, memberList.get(0)) );
        joinBoardResponseList.add(joinBoardService.createJoinBoardByDto(req2, memberList.get(0)) );

        joinBoardResponseList.add(joinBoardService.createJoinBoardByDto(req3, memberList.get(1)) );
        joinBoardResponseList.add(joinBoardService.createJoinBoardByDto(req4, memberList.get(1)) );
        log.info("==========BeforeEach 2끝==========");

    }


    @AfterEach
    void tearDown() {
        joinBoardRepository.deleteAll();
        memberRepository.deleteAll();
        profileImgRepository.deleteAll();
    }



    @Test
    @DisplayName("getJoinBoardEntityById 메서드 테스트1 - id로 JoinBoard 엔티티를 찾아오는 메서드이고, 찾지못할시의 예외도 테스트 한다.")
    void getJoinBoardEntityById_method_test1() throws Exception {
        // given
        // BeforeEach로 인해 2명의 멤버와, 멤버 1명당 2개씩의 조인보드 저장

        // when
        JoinBoard get1 = joinBoardService.getJoinBoardEntityById(joinBoardResponseList.getFirst().getId());

        // then
        assertThat(get1.getId()).isEqualTo(joinBoardResponseList.getFirst().getId());

        // 설정해둔 예외대로 잘 발생하는지도 체크
        assertThatThrownBy
                (() -> joinBoardService.getJoinBoardEntityById(joinBoardResponseList.getLast().getId()+1L))
                .isInstanceOf(JoinBoardNotFoundException.class)
                .hasMessage(ExceptionMessage.JoinBoard.JOIN_BOARD_NOT_FOUND_ERROR);

        // 결과: ok

    }


    @Test
    @DisplayName("JoinBoard 엔티티 내부에서 작성자(멤버) 엔티티를 잘 가지고 있는지 테스트")
    void joinBoard_member_entity_test1() throws Exception {

        // given
        // BeforeEach로 인해 2명의 멤버와, 멤버 1명당 2개씩의 조인보드 저장

        // when
        JoinBoard get1 = joinBoardService.getJoinBoardEntityById(joinBoardResponseList.get(0).getId());
        JoinBoard get2 = joinBoardService.getJoinBoardEntityById(joinBoardResponseList.get(1).getId());
        JoinBoard get3 = joinBoardService.getJoinBoardEntityById(joinBoardResponseList.get(2).getId());
        JoinBoard get4 = joinBoardService.getJoinBoardEntityById(joinBoardResponseList.get(3).getId());

        // then
        assertThat(get1.getMember().getMemberId()).isEqualTo(memberList.get(0).getMemberId());
        assertThat(get2.getMember().getMemberId()).isEqualTo(memberList.get(0).getMemberId());

        assertThat(get3.getMember().getMemberId()).isEqualTo(memberList.get(1).getMemberId());
        assertThat(get4.getMember().getMemberId()).isEqualTo(memberList.get(1).getMemberId());

        log.info("get1.getMember().getMemberId() = {}", get1.getMember().getMemberId());
        log.info("memberList.get(0).getMemberId() = {}", memberList.get(0).getMemberId());

        log.info("get1.getMember().getMemberId() = {}", get3.getMember().getMemberId());
        log.info("memberList.get(0).getMemberId() = {}", memberList.get(1).getMemberId());



    }


    @Test
    @DisplayName("createJoinBoardByDto 메서드 테스트1 - 유저의 입력값을 Request Dto로 받고, 작성자(멤버)와 같이 글을 저장하는 메서드")
    void createJoinBoardByDto_method_test1() throws Exception {
        // given
        // BeforeEach로 인해 2명의 멤버와, 멤버 1명당 2개씩의 조인보드 저장

    	  // when
        JoinBoardRegisterRequest req = JoinBoardRegisterRequest.builder()
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

        JoinBoardResponse res = joinBoardService.createJoinBoardByDto(req, memberList.get(0));
        joinBoardResponseList.add(res);

        JoinBoard find1 = joinBoardRepository.findById(res.getId())
                .orElseThrow(() -> new JoinBoardNotFoundException(ExceptionMessage.JoinBoard.JOIN_BOARD_NOT_FOUND_ERROR));

        // then
        assertThat(res.getId()).isEqualTo(find1.getId());
        assertThat(res.getContent()).isEqualTo(req.getContent());
        assertThat(find1.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(res.getMember().getMemberId()).isEqualTo(find1.getMember().getMemberId());

    }




    @Test
    @DisplayName("getJoinBoardEntityByIdWithNoDeleted 메서드 테스트1 - id로 JoinBoard 엔티티를 찾아오는데 딜리트처리 안된것만 찾아오는 메서드 - ACTIVE 상태의 글을 찾는 상황")
    void getJoinBoardEntityByIdWithNoDeleted_method_test1() throws Exception {
        // given
        // BeforeEach로 인해 2명의 멤버와, 멤버 1명당 2개씩의 조인보드 저장

        // when
        JoinBoard get1 = joinBoardService.getJoinBoardEntityByIdWithNoDeleted(joinBoardResponseList.get(0).getId());

        // then
        assertThat(get1.getId()).isEqualTo(joinBoardResponseList.get(0).getId());
        log.info("get1.getContent() = {}", get1.getContent());

    }




    @Test
    @DisplayName("getJoinBoardEntityByIdWithNoDeleted 메서드 테스트2 - id로 JoinBoard 엔티티를 찾아오는데 딜리트처리 안된것만 찾아오는 메서드 -> delete 된걸 찾으려할때 예외 잘 발생하는지 테스트")
    void getJoinBoardEntityByIdWithNoDeleted_method_test2() throws Exception {
        // given
        // BeforeEach로 인해 2명의 멤버와, 멤버 1명당 2개씩의 조인보드 저장

        JoinBoard find1 = joinBoardRepository.findById(joinBoardResponseList.get(0).getId())
                .orElseThrow(() -> new JoinBoardNotFoundException(ExceptionMessage.JoinBoard.JOIN_BOARD_NOT_FOUND_ERROR));

        // when
        find1.changeStatus(Status.DELETE);
        joinBoardRepository.save(find1);

        // then
        assertThatThrownBy
                ( () -> joinBoardService.getJoinBoardEntityByIdWithNoDeleted( joinBoardResponseList.get(0).getId() ))
                .isInstanceOf(JoinBoardNotFoundException.class)
                .hasMessage(ExceptionMessage.JoinBoard.JOIN_BOARD_NOT_FOUND_ERROR);

    }



    @Test
    @DisplayName("getJoinBoardById 메서드 테스트1 - id로 JoinBoard 엔티티를 delete 아닌것만 찾고 응답dto로 반환하는 메서드")
    void getJoinBoardById_method_test1() throws Exception {
        // given
        // BeforeEach로 인해 2명의 멤버와, 멤버 1명당 2개씩의 조인보드 저장

    	  // when
        JoinBoardResponse res = joinBoardService.getJoinBoardById(joinBoardResponseList.get(0).getId());

        // then
        assertThat(res.getId()).isEqualTo(joinBoardResponseList.get(0).getId());
        assertThat(res.getContent()).isEqualTo(joinBoardResponseList.get(0).getContent());

    }


    @Test
    @DisplayName("getJoinBoardById 메서드 테스트2 - id로 JoinBoard 엔티티를 delete 아닌것만 찾고 응답dto로 반환하는 메서드 -> delete 된걸 찾으려할때 예외 잘 발생하는지 테스트")
    void getJoinBoardById_method_test2() throws Exception {

        // given
        // BeforeEach로 인해 2명의 멤버와, 멤버 1명당 2개씩의 조인보드 저장

        JoinBoard find1 = joinBoardRepository.findById(joinBoardResponseList.get(0).getId())
                .orElseThrow(() -> new JoinBoardNotFoundException(ExceptionMessage.JoinBoard.JOIN_BOARD_NOT_FOUND_ERROR));

        // when
        find1.changeStatus(Status.DELETE);
        joinBoardRepository.save(find1);

        // then
        assertThatThrownBy
                ( () -> joinBoardService.getJoinBoardById(joinBoardResponseList.get(0).getId() ))
                .isInstanceOf(JoinBoardNotFoundException.class)
                .hasMessage(ExceptionMessage.JoinBoard.JOIN_BOARD_NOT_FOUND_ERROR);

    }

    @Test
    @DisplayName("getAllJoinBoard 메서드 테스트1 -  DB에서 글을 전부 찾아 JoinBoardResponse 로 변환후 리스트에 담아 반환하는 메서드 -> DELETE 필터링까지 잘 되는지 체크중")
    void getAllJoinBoard_method_test1() throws Exception {
        // given
        // BeforeEach로 인해 2명의 멤버와, 멤버 1명당 2개씩의 조인보드 저장된 상태다.


        // 저장되어있는 DB의 첫번째 엔티티를 찾아와 delete 처리하기.
        JoinBoard findJoinBoard = joinBoardRepository.findById(joinBoardResponseList.get(0).getId()).orElseThrow();

        findJoinBoard.changeStatus(Status.DELETE);

        joinBoardRepository.save(findJoinBoard);


        // when
        List<JoinBoardResponse> list = joinBoardService.getAllJoinBoard();

        // then
        assertThat(list.size()).isEqualTo(3);

        JoinBoardResponse res = list.get(0);
        assertThat(res.getId()).isEqualTo(joinBoardResponseList.get(1).getId());

    }


    @Test
    @DisplayName("getAllJoinBoard 메서드 테스트2 - 해당하는것이 없을때 빈 리스트를 잘 반환하는지 확인하는 메서드")
    void getAllJoinBoard_method_test2() throws Exception {
        // given
        // BeforeEach로 인해 2명의 멤버와, 멤버 1명당 2개씩의 조인보드 저장된 상태고, DB를 아예 지울것임.

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
        // BeforeEach로 인해 2명의 멤버와, 멤버 1명당 2개씩의 조인보드 저장된 상태다.


        // 저장되어있는 DB의 첫번째 엔티티를 찾아와 delete 처리하기.
        JoinBoard findJoinBoard = joinBoardRepository.findById(joinBoardResponseList.get(0).getId()).orElseThrow();

        findJoinBoard.changeStatus(Status.DELETE);

        joinBoardRepository.save(findJoinBoard);


        // when
        List<JoinBoardCardResponse> list = joinBoardService.getAllJoinBoardCard();

        // then
        assertThat(list.size()).isEqualTo(3);

        JoinBoardCardResponse res = list.get(0);
        assertThat(res.getId()).isEqualTo(joinBoardResponseList.get(1).getId());

    }





    @Test
    @DisplayName("getAllJoinBoardCard 메서드 테스트2 - 해당하는것이 없을때 빈 리스트를 잘 반환하는지 확인하는 메서드")
    void getAllJoinBoardCard_method_test2() throws Exception {
        // given
        // BeforeEach로 인해 2명의 멤버와, 멤버 1명당 2개씩의 조인보드 저장된 상태고, DB를 아예 지울것임.
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
        // BeforeEach로 인해 2명의 멤버와, 멤버 1명당 2개씩의 조인보드 저장된 상태다.


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

        JoinBoardResponse res = joinBoardService.updateJoinBoardById(joinBoardResponseList.get(0).getId(), build);

        JoinBoardResponse find = joinBoardService.getJoinBoardById(res.getId());

        // then
        assertThat(res.getId()).isEqualTo( find.getId() );
        assertThat(res.getContent()).isEqualTo(find.getContent());

        log.info("res.getTitle() = {}", res.getTitle());
        log.info("res.getTopic() = {}", res.getTopic());

        // 결과: ok

    }



    @Test
    @DisplayName("deleteJoinBoardById 메서드 테스트1 - 기존 엔티티를 softDelete 처리하는 메서드 (즉 Enum타입의 status를 ACTIVE에서 DELETE로 처리) ")
    void deleteJoinBoardById_method_test1() throws Exception {

        // given
        // BeforeEach로 인해 2명의 멤버와, 멤버 1명당 2개씩의 조인보드 저장된 상태다.

    	  // when
        JoinBoard find1 = joinBoardRepository.findById(joinBoardResponseList.get(0).getId())
                .orElseThrow(() -> new JoinBoardNotFoundException(ExceptionMessage.JoinBoard.JOIN_BOARD_NOT_FOUND_ERROR));

        // 기존 status는 ACTIVE 인지 확인
        assertThat(find1.getStatus()).isEqualTo(Status.ACTIVE);
        log.info("find1.getStatus() = {}", find1.getStatus());

        joinBoardService.deleteJoinBoardById(joinBoardResponseList.get(0).getId());

        JoinBoard find2 = joinBoardRepository.findById(joinBoardResponseList.get(0).getId())
                .orElseThrow(() -> new JoinBoardNotFoundException(ExceptionMessage.JoinBoard.JOIN_BOARD_NOT_FOUND_ERROR));

        // then
        assertThat(find2.getStatus()).isEqualTo(Status.DELETE);
        log.info("find2.getStatus() = {}", find2.getStatus());

        // 결과: ok
    }





}