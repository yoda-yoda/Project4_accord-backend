package org.noteam.be.admin;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.noteam.be.admin.service.AdminBoardService;
import org.noteam.be.joinBoard.domain.JoinBoard;
import org.noteam.be.joinBoard.repository.JoinBoardRepository;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.Role;
import org.noteam.be.member.domain.Status;
import org.noteam.be.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class AdminBoardServiceTest {

    @Autowired
    private AdminBoardService adminBoardService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JoinBoardRepository joinBoardRepository;

    @AfterEach
    void cleanUp() {
        joinBoardRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 정상 삭제 확인")
    void deleteBoardSuccessfully() {
        // Given
        JoinBoard board = createTestBoard(createTestMember("user@test.com"), "삭제대상 게시글");

        // When
        adminBoardService.deleteBoard(board.getId());

        // Then
        assertThat(joinBoardRepository.findById(board.getId())).isEmpty();
        log.info("게시글 삭제 테스트 성공");
    }

    @Test
    @DisplayName("게시글 삭제+작성자 밴 검증")
    void deleteBoardAndBanUser() {
        // Given
        Member author = createTestMember("author@test.com");
        JoinBoard board = createTestBoard(author, "문제 게시글");

        // When
        adminBoardService.deleteBoardAndBanUser(board.getId());

        // Then
        Member updatedMember = memberRepository.findById(author.getMemberId()).get();
        assertThat(updatedMember.getStatus()).isEqualTo(Status.BANNED);
        log.info("게시글 삭제+작성자 밴 테스트 성공");
    }

    private Member createTestMember(String email) {
        return memberRepository.save(Member.builder()
                .email(email)
                .nickname("테스트유저")
                .status(Status.ACTIVE)
                .role(Role.MEMBER)
                .provider("local")
                .build());
    }

    private JoinBoard createTestBoard(Member member, String title) {
        return joinBoardRepository.save(JoinBoard.builder()
                .member(member)
                .title(title)
                .teamName("테스트팀")
                .content("테스트 content")
                .topic("테스트 topic")
                .projectBio("프로젝트 소개")
                .teamBio("팀 소개")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7))
                .build());
    }

}