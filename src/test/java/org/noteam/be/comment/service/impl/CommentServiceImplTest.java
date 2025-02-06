package org.noteam.be.comment.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.noteam.be.comment.domain.Comment;
import org.noteam.be.comment.domain.Status;
import org.noteam.be.comment.dto.CommentRegisterRequest;
import org.noteam.be.comment.dto.CommentResponse;
import org.noteam.be.comment.repository.CommentRepository;
import org.noteam.be.comment.service.CommentService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// ddl auto - create 로 테스트 중
@SpringBootTest
@Slf4j
class CommentServiceImplTest {

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
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;




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



    @BeforeEach
    @DisplayName("총 2명의 멤버가 본인글과 타인의 글에 각각 댓글 1개씩 저장")
    void setUp3() {

        // given
        // BeforeEach로 인해 2명의 멤버와, 멤버 1명당 2개씩의 조인보드 저장

        CommentRegisterRequest dto1 = CommentRegisterRequest.builder()
                .content("content1")
                .build();

        CommentRegisterRequest dto2 = CommentRegisterRequest.builder()
                .content("content2")
                .build();

        CommentRegisterRequest dto3 = CommentRegisterRequest.builder()
                .content("content3")
                .build();

        CommentRegisterRequest dto4 = CommentRegisterRequest.builder()
                .content("content4")
                .build();

        Member mem1 = memberList.get(0); // 멤버1
        Member mem2 = memberList.get(1); // 멤버2

        JoinBoardResponse joinBoardResponse1 = joinBoardResponseList.get(0); // 멤버 1이 작성한 글응답
        JoinBoardResponse joinBoardResponse2 = joinBoardResponseList.get(2); // 멤버 2가 작성한 글응답


        // when
        CommentResponse res1 = commentService.createCommentByDto(dto1, mem1, joinBoardResponse1.getId());
        CommentResponse res2 = commentService.createCommentByDto(dto2, mem1, joinBoardResponse2.getId());
        CommentResponse res3 = commentService.createCommentByDto(dto3, mem2, joinBoardResponse1.getId());
        CommentResponse res4 = commentService.createCommentByDto(dto4, mem2, joinBoardResponse2.getId());

        commentResponseList.add(res1);
        commentResponseList.add(res2);
        commentResponseList.add(res3);
        commentResponseList.add(res4);


        // then
        assertThat(res1.getMemberId()).isEqualTo(mem1.getMemberId());
        assertThat(res1.getMemberNickname()).isEqualTo(mem1.getNickname());

        assertThat(res2.getMemberId()).isEqualTo(mem1.getMemberId());
        assertThat(res2.getMemberNickname()).isEqualTo(mem1.getNickname());

        assertThat(res3.getMemberId()).isEqualTo(mem2.getMemberId());
        assertThat(res3.getMemberNickname()).isEqualTo(mem2.getNickname());

        assertThat(res4.getMemberId()).isEqualTo(mem2.getMemberId());
        assertThat(res4.getMemberNickname()).isEqualTo(mem2.getNickname());

        log.info("==========BeforeEach 3끝==========");

    }

//    @AfterEach
//    void tearDown() {
//        joinBoardRepository.deleteAll();
//        memberRepository.deleteAll();
//        profileImgRepository.deleteAll();
//    }


    @Test
    @DisplayName("createCommentByDto 메서드 테스트1 - 댓글 입력 dto, 멤버, 조인보드id를 매개변수로 받은후 결과적으로 댓글 엔티티를 만들고 댓글 DB에 저장하는 메서드. 그리고 응답객체에 해당멤버가 잘 할당되는지 확인")
    void createCommentByDto_method_test1() throws Exception {
        // given
        // 현재 BeforeEach로 인해 => (2명의 멤버 저장) + (멤버 1명당 2개의 조인보드 저장) + (조인보드 2개에 각각 멤버1,2가 쓴 댓글이 존재하도록 저장)


        CommentRegisterRequest dto = CommentRegisterRequest.builder()
                .content("content")
                .build();

        Member mem1 = memberList.get(0);

        JoinBoardResponse joinBoardResponse = joinBoardResponseList.get(0);


        // when
        CommentResponse res = commentService.createCommentByDto(dto, mem1, joinBoardResponse.getId());

        // then
        assertThat(res.getMemberId()).isEqualTo(mem1.getMemberId());
        assertThat(res.getMemberNickname()).isEqualTo(mem1.getNickname());

    }


    @Test
    @DisplayName("createCommentByDto 메서드 테스트2 - 같은 멤버가 같은 글에 댓글 3개 저장한다. 그리고 응답객체에 해당멤버가 잘 할당되는지 확인")
    void createCommentByDto_method_test2() throws Exception {
        // given
        // 현재 BeforeEach로 인해 => (2명의 멤버 저장) + (멤버 1명당 2개의 조인보드 저장) + (조인보드 2개에 각각 멤버1,2가 쓴 댓글이 존재하도록 저장)


        CommentRegisterRequest dto1 = CommentRegisterRequest.builder()
                .content("content1")
                .build();

        CommentRegisterRequest dto2 = CommentRegisterRequest.builder()
                .content("content2")
                .build();

        CommentRegisterRequest dto3 = CommentRegisterRequest.builder()
                .content("content3")
                .build();

        Member mem1 = memberList.get(0);

        JoinBoardResponse joinBoardResponse = joinBoardResponseList.get(0);


        // when
        CommentResponse res1 = commentService.createCommentByDto(dto1, mem1, joinBoardResponse.getId());
        CommentResponse res2 = commentService.createCommentByDto(dto2, mem1, joinBoardResponse.getId());
        CommentResponse res3 = commentService.createCommentByDto(dto3, mem1, joinBoardResponse.getId());

        // then
        assertThat(res1.getMemberId()).isEqualTo(mem1.getMemberId());
        assertThat(res1.getMemberNickname()).isEqualTo(mem1.getNickname());

        assertThat(res2.getMemberId()).isEqualTo(mem1.getMemberId());
        assertThat(res2.getMemberNickname()).isEqualTo(mem1.getNickname());

        assertThat(res3.getMemberId()).isEqualTo(mem1.getMemberId());
        assertThat(res3.getMemberNickname()).isEqualTo(mem1.getNickname());

    }


    @Test
    @DisplayName("createCommentByDto 메서드 테스트3 - 총 2명의 멤버가 2개의 글에 각자 댓글 2개씩 저장하는 메서드. 그리고 응답객체에 해당멤버가 잘 할당되는지 확인")
    void createCommentByDto_method_test3() throws Exception {
        // given
        // 현재 BeforeEach로 인해 => (2명의 멤버 저장) + (멤버 1명당 2개의 조인보드 저장) + (조인보드 2개에 각각 멤버1,2가 쓴 댓글이 존재하도록 저장)

        CommentRegisterRequest dto1 = CommentRegisterRequest.builder()
                .content("content1")
                .build();

        CommentRegisterRequest dto2 = CommentRegisterRequest.builder()
                .content("content2")
                .build();

        CommentRegisterRequest dto3 = CommentRegisterRequest.builder()
                .content("content3")
                .build();

        CommentRegisterRequest dto4 = CommentRegisterRequest.builder()
                .content("content4")
                .build();

        Member mem1 = memberList.get(0); // 멤버1
        Member mem2 = memberList.get(1); // 멤버2

        JoinBoardResponse joinBoardResponse1 = joinBoardResponseList.get(0); // 멤버 1이 작성한 글응답
        JoinBoardResponse joinBoardResponse2 = joinBoardResponseList.get(2); // 멤버 2가 작성한 글응답


        // when
        CommentResponse res1 = commentService.createCommentByDto(dto1, mem1, joinBoardResponse1.getId());
        CommentResponse res2 = commentService.createCommentByDto(dto2, mem1, joinBoardResponse2.getId());
        CommentResponse res3 = commentService.createCommentByDto(dto3, mem2, joinBoardResponse1.getId());
        CommentResponse res4 = commentService.createCommentByDto(dto4, mem2, joinBoardResponse2.getId());

        // then
        assertThat(res1.getMemberId()).isEqualTo(mem1.getMemberId());
        assertThat(res1.getMemberNickname()).isEqualTo(mem1.getNickname());

        assertThat(res2.getMemberId()).isEqualTo(mem1.getMemberId());
        assertThat(res2.getMemberNickname()).isEqualTo(mem1.getNickname());

        assertThat(res3.getMemberId()).isEqualTo(mem2.getMemberId());
        assertThat(res3.getMemberNickname()).isEqualTo(mem2.getNickname());

        assertThat(res4.getMemberId()).isEqualTo(mem2.getMemberId());
        assertThat(res4.getMemberNickname()).isEqualTo(mem2.getNickname());

    }



    @Test
    @DisplayName("deleteCommentById 메서드 테스트1 - 해당 id의 Comment 를 소프트 딜리트 처리하는 메서드 테스트")
    void deleteCommentById_method_test1() throws Exception {
        // given
        // 현재 BeforeEach로 인해 => (2명의 멤버 저장) + (멤버 1명당 2개의 조인보드 저장) + (조인보드 2개에 각각 멤버1,2가 쓴 댓글이 존재하도록 저장)

        JoinBoardResponse joinBoardResponse = joinBoardResponseList.get(0);
        Member mem1 = memberList.get(0);
        Member mem2 = memberList.get(1);


        // when
        commentService.deleteCommentById( commentResponseList.get(0).getId() );

        Comment findComment = commentRepository.findById(commentResponseList.get(0).getId())
                .orElseThrow();


        // then
        assertThat(findComment.getStatus()).isEqualTo(Status.DELETE);
        log.info("findComment.getStatus() = {}", findComment.getStatus());

    }





    @Test
    @DisplayName("getAllCommentByJoinBoardId 메서드 테스트1 - DB에서, 해당 JoinBoard에 속하는 댓글 엔티티 ACTIVE만 전부 찾아 응답으로 변환후 리스트에 담아 반환하는 메서드 테스트")
    void getAllCommentByJoinBoardId_method_test1() throws Exception {
        // given
        // 현재 BeforeEach로 인해 => (2명의 멤버 저장) + (멤버 1명당 2개의 조인보드 저장) + (조인보드 2개에 각각 멤버1,2가 쓴 댓글이 존재하도록 저장)

        JoinBoardResponse joinBoardResponse = joinBoardResponseList.get(0);
        Member mem1 = memberList.get(0);
        Member mem2 = memberList.get(1);

        // when
        List<CommentResponse> list = commentService.getAllCommentByJoinBoardId(joinBoardResponse.getId());

        // then
        assertThat(list.size()).isEqualTo(2);

        CommentResponse res1 = list.get(0);
        CommentResponse res2 = list.get(1);

        assertThat(res1.getMemberId()).isEqualTo(mem1.getMemberId());
        assertThat(res2.getMemberId()).isEqualTo(mem2.getMemberId());

    }


    @Test
    @DisplayName("getAllCommentByJoinBoardId 메서드 테스트2 - 해당 글에 속한 댓글을 찾아올때 DELETE된 댓글도 잘 필터링 잘하는지 테스트")
    void getAllCommentByJoinBoardId_method_test2() throws Exception {

        // given
        // 현재 BeforeEach로 인해 => (2명의 멤버 저장) + (멤버 1명당 2개의 조인보드 저장) + (조인보드 2개에 각각 멤버1,2가 쓴 댓글이 존재하도록 저장)

        JoinBoardResponse joinBoardResponse = joinBoardResponseList.get(0);
        log.info("joinBoardResponse.getId() = {}", joinBoardResponse.getId());
        Member mem1 = memberList.get(0);
        Member mem2 = memberList.get(1);


        // DB의 댓글 하나 삭제처리
        commentService.deleteCommentById( commentResponseList.get(0).getId() );

        Comment findComment = commentRepository.findById(commentResponseList.get(0).getId())
                .orElseThrow();

        assertThat(findComment.getStatus()).isEqualTo(Status.DELETE);
        log.info("findComment.getStatus() = {}", findComment.getStatus());



        // when
        Long joinBoardId = commentResponseList.get(0).getJoinBoardId();

        List<CommentResponse> list = commentService.getAllCommentByJoinBoardId( joinBoardId );


        // then
        assertThat(list.size()).isEqualTo(1);
        CommentResponse res1 = list.get(0);
        assertThat(res1.getMemberId()).isEqualTo(mem2.getMemberId());

    }






}