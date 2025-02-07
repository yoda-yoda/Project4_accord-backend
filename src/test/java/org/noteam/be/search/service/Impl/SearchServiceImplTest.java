package org.noteam.be.search.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.noteam.be.comment.dto.CommentResponse;
import org.noteam.be.joinBoard.dto.JoinBoardCardResponse;
import org.noteam.be.joinBoard.dto.JoinBoardRegisterRequest;
import org.noteam.be.joinBoard.dto.JoinBoardResponse;
import org.noteam.be.joinBoard.repository.JoinBoardRepository;
import org.noteam.be.joinBoard.service.Impl.JoinBoardServiceImpl;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.Role;
import org.noteam.be.member.repository.MemberRepository;
import org.noteam.be.member.service.MemberService;
import org.noteam.be.profileimg.entity.ProfileImg;
import org.noteam.be.profileimg.repository.ProfileImgRepository;
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
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProfileImgRepository profileImgRepository;


    // BeforeEach 내부에서 생성한 엔티티를 저장해놓고, 테스트에 사용하기위한 변수들
    List<JoinBoardResponse> joinBoardResponseList = new ArrayList<>();
    List<Member> memberList = new ArrayList<>();
    List<CommentResponse> commentResponseList = new ArrayList<>();



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
    void setUp2() {

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


        JoinBoardResponse joinBoardRes1 = joinBoardService.createJoinBoardByDto(req1, memberList.get(0));
        JoinBoardResponse joinBoardRes2 = joinBoardService.createJoinBoardByDto(req2, memberList.get(0));
        JoinBoardResponse joinBoardRes3 = joinBoardService.createJoinBoardByDto(req3, memberList.get(0));

        joinBoardService.deleteJoinBoardById(joinBoardRes3.getId());

        log.info("==========BeforeEach2 끝==========");

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