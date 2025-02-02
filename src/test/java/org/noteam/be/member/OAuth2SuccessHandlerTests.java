package org.noteam.be.member;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.Role;
import org.noteam.be.member.domain.Status;
import org.noteam.be.member.dto.CustomUserDetails;
import org.noteam.be.member.repository.MemberRepository;
import org.noteam.be.member.repository.RefreshTokenRepository;
import org.noteam.be.system.config.JwtConfiguration;
import org.noteam.be.system.security.OAuth2SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class OAuth2SuccessHandlerTests {

    @Autowired
    private OAuth2SuccessHandler oAuth2SuccessHandler;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtConfiguration jwtConfiguration;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Value("${custom.frontend.redirect-uri}")
    private String baseRedirectUri;

    @AfterEach
    void cleanUp() {
        // 테스트마다 DB 정리
        refreshTokenRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("OAuth2 인증 성공 시 Access/Refresh 토큰이 쿠키로 생성되고, 지정된 URL로 리디렉션 테스트")
    void onAuthenticationSuccessCreatesCookiesAndRedirects() throws IOException, ServletException {
        // given
        // 테스트용 Member 생성 및 저장
        Member member = Member.of(
                "testUser@kakao.com",
                "TestNickname",
                Role.MEMBER,
                Status.ACTIVE,
                "kakao"
        );
        Member savedMember = memberRepository.save(member);

        // CustomUserDetails 준비
        CustomUserDetails principal = CustomUserDetails.builder()
                .memberId(savedMember.getMemberId())
                .email(savedMember.getEmail())
                .nickname(savedMember.getNickname())
                .role(savedMember.getRole())
                .attributes(null)
                .build();

        // Authentication 셋팅
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        // Mock 요청 + 응답
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        oAuth2SuccessHandler.onAuthenticationSuccess(request, response, authentication);

        // then
        // 쿠키가 2개(accessToken, refreshToken) 추가되었는지 검증
        Cookie[] cookies = response.getCookies();
        assertThat(cookies).hasSize(2);

        Cookie accessCookie = Arrays.stream(cookies)
                .filter(c -> c.getName().equals("accessToken"))
                .findFirst()
                .orElse(null);

        Cookie refreshCookie = Arrays.stream(cookies)
                .filter(c -> c.getName().equals("refreshToken"))
                .findFirst()
                .orElse(null);

        // Access 쿠키 유효성 검사
        assertThat(accessCookie).isNotNull();
        assertThat(accessCookie.getValue()).isNotEmpty();
        assertThat(accessCookie.getMaxAge())
                .isEqualTo((int) (jwtConfiguration.getValidation().getAccess() / 1000));

        // Refresh 쿠키 유효성 검사
        assertThat(refreshCookie).isNotNull();
        assertThat(refreshCookie.getValue()).isNotEmpty();
        assertThat(refreshCookie.getMaxAge())
                .isEqualTo((int) (jwtConfiguration.getValidation().getRefresh() / 1000));

        // 리다이렉트 URL 확인
        assertThat(response.getRedirectedUrl()).isEqualTo(baseRedirectUri);
    }


}