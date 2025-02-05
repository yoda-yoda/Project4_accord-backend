package org.noteam.be.admin;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.noteam.be.admin.dto.MemberSearchResponse;
import org.noteam.be.admin.service.AdminMemberService;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.Role;
import org.noteam.be.member.domain.Status;
import org.noteam.be.member.repository.MemberRepository;
import org.noteam.be.system.exception.member.MemberNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
class AdminMemberServiceTest {

    @Autowired
    private AdminMemberService adminMemberService;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void cleanUp() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 검색 닉네임 일치/부분일치 정렬 확인")
    void searchMembersWithNicknameOrdering() {
        // Given
        createTestMember("일치", "exact@test.com");
        createTestMember("부분일치테스트", "partial@test.com");

        // When
        Page<MemberSearchResponse> result = adminMemberService.searchMembers(
                "일치",
                PageRequest.of(0, 10)
        );

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getNickname()).isEqualTo("일치");
        assertThat(result.getContent().get(1).getNickname()).isEqualTo("부분일치테스트");
        log.info("회원 검색 닉네임 일치/부분일치 정렬 테스트 성공");
    }

    @Test
    @DisplayName("회원 상태 변경 ACTIVE → BANNED")
    void updateMemberStatusToBanned() {
        // Given
        Member member = createTestMember("testuser","status@test.com");

        // When
        adminMemberService.updateMemberStatus(member.getMemberId(), Status.BANNED);

        // Then
        Member updated = memberRepository.findById(member.getMemberId()).get();
        assertThat(updated.getStatus()).isEqualTo(Status.BANNED);
        log.info("회원 상태 변경 ACTIVE → BANNED 테스트 성공");
    }

    @Test
    @DisplayName("존재하지 않는 회원 상태 변경 시 예외 발생")
    void updateNonExistingMemberStatus() {
        // When & Then
        assertThrows(MemberNotFoundException.class, () -> {
            adminMemberService.updateMemberStatus(999L, Status.BANNED);
        });

    }

    private Member createTestMember(String nickname, String email) {
        return memberRepository.save(Member.builder()
                .email(email)
                .nickname(nickname)
                .status(Status.ACTIVE)
                .role(Role.MEMBER)
                .provider("local")
                .build());
    }
}