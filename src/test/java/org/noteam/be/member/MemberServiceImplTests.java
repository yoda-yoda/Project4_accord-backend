package org.noteam.be.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.Role;
import org.noteam.be.member.domain.Status;
import org.noteam.be.member.dto.NicknameUpdateRequest;
import org.noteam.be.member.dto.OAuthSignUpRequest;
import org.noteam.be.member.repository.MemberRepository;
import org.noteam.be.member.service.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class MemberServiceImplTests {

    @Autowired
    private MemberServiceImpl memberService;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void cleanUp() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("OAuth 회원 가입 성공")
    void registerOAuthMemberSuccessfully() {
        // given
        OAuthSignUpRequest request = OAuthSignUpRequest.builder()
                .email("testuser@kakao.com")
                .nickname("TestUser")
                .status(Status.ACTIVE)
                .build();
        String provider = "kakao";

        // when
        Member savedMember = memberService.registerOAuthMember(request, provider);

        // then
        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getEmail()).isEqualTo(request.getEmail());
        assertThat(savedMember.getNickname()).isEqualTo(request.getNickname());
        assertThat(savedMember.getRole().name()).isEqualTo("MEMBER");
    }

    @Test
    @DisplayName("OAuth 사용자 로드 테스트")
    void loadOAuthUserSuccessfully() {
        // given
        OAuthSignUpRequest request = OAuthSignUpRequest.builder()
                .email("testuser@google.com")
                .nickname("TestUser")
                .status(Status.ACTIVE)
                .build();
        String provider = "google";
        memberService.registerOAuthMember(request, provider);

        // when
        Member foundMember = memberRepository.findByEmail(request.getEmail()).orElse(null);

        // then
        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getEmail()).isEqualTo(request.getEmail());
        assertThat(foundMember.getProvider()).isEqualTo(provider);
    }
    
    @Test
    @DisplayName("닉네임 변경 테스트")
    void updateNicknameSuccessfully() {
        // given
        Member member = Member.of(
                "test@google.com", "닉네임변경예정", Role.MEMBER, Status.ACTIVE, "google");

        Member savedMember = memberRepository.save(member);
        String newNickname = "닉네임변경완료";

        // when
        NicknameUpdateRequest request = new NicknameUpdateRequest(newNickname);
        memberService.updateNickname(savedMember.getMemberId(), request);

        // then
        // 1. Null 인지 아닌지 체크
        // 2. 닉네임 변경됐는지 확인
        Member updatedMember = memberRepository.findById(savedMember.getMemberId()).orElse(null);
        assertThat(updatedMember).isNotNull();
        assertThat(updatedMember.getNickname()).isEqualTo(newNickname);
    }

    @Test
    @DisplayName("회원 탈퇴 테스트")
    void deleteMemberSuccessfully(){
        // given
        Member member = Member.of(
                "test@google.com","nickname",Role.MEMBER, Status.ACTIVE, "google");
        Member savedMember = memberRepository.save(member);

        // when
        memberService.deleteMember(savedMember.getMemberId());

        // then
        Member deletedMember = memberRepository.findById(savedMember.getMemberId()).orElse(null);
        assertThat(deletedMember).isNotNull();
        assertThat(deletedMember.getStatus()).isEqualTo(Status.DELETED);

    }

}