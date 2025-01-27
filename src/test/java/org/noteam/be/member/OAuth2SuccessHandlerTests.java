package org.noteam.be.member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.Role;
import org.noteam.be.member.dto.CustomUserDetails;
import org.noteam.be.member.repository.MemberRepository;
import org.noteam.be.member.service.JwtTokenProvider;
import org.noteam.be.system.config.JwtConfiguration;
import org.noteam.be.system.security.OAuth2SuccessHandler;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class OAuth2SuccessHandlerTests {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private JwtConfiguration jwtConfiguration;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private OAuth2SuccessHandler successHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // validation 객체 생성 및 설정
        JwtConfiguration.Validation validation = new JwtConfiguration.Validation();
        validation.setAccess(60000L); // 1분
        validation.setRefresh(120000L); // 2분

        // secret 객체 생성 및 설정
        JwtConfiguration.Secret secret = new JwtConfiguration.Secret();
        secret.setAppKey("your-256-bit-secret-key-for-testing-purposes-only");

        // mock 객체 동작 정의
        when(jwtConfiguration.getValidation()).thenReturn(validation);
        when(jwtConfiguration.getSecret()).thenReturn(secret);

        // OAuth2SuccessHandler 인스턴스 생성 tokenprovider, configuration
        successHandler = new OAuth2SuccessHandler(jwtTokenProvider, jwtConfiguration, memberRepository);

        ReflectionTestUtils.setField(
                successHandler,
                "baseRedirectUri",
                "http://localhost:3000/auth");

    }

    @Test
    @DisplayName("onAuthenticationSuccess - 쿠키 + 리다이렉트 처리테스트")
    void onAuthenticationSuccess_SetsCookiesAndRedirects() throws Exception {
        // given
        Long memberId = 1L;
        String email = "testuser@example.com";
        String nickname = "TestUser";
        Role role = Role.MEMBER;
        Map<String, Object> attributes = Map.of("customKey", "customValue"); // 테스트용
        String encodedUrl = "http://localhost:3000/auth"; // url검증을 하기 위해서 추가.

        CustomUserDetails principal = CustomUserDetails.builder()
                .memberId(memberId)
                .email(email)
                .nickname(nickname)
                .role(role)
                .attributes(attributes)
                .build();

        String accessToken = Jwts.builder()
                .subject(memberId.toString())
                .claim("role", role.name())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime()+60000))
                .signWith(Keys.hmacShaKeyFor("your-256-bit-secret-key-for-testing-purposes-only".getBytes()), Jwts.SIG.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .subject(memberId.toString())
                .claim("role", role.name())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime()+120000))
                .signWith(Keys.hmacShaKeyFor("your-256-bit-secret-key-for-testing-purposes-only".getBytes()), Jwts.SIG.HS256)
                .compact();

        // Member 엔티티 Mock 리턴
        Member fakeMember = Member.builder()
                .email(email)
                .nickname(nickname)
                .role(Role.MEMBER)
                .provider("google")
                .build();
        ReflectionTestUtils.setField(fakeMember, "memberId", memberId);

        when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(fakeMember));
        when(authentication.getPrincipal()).thenReturn(principal);
        when(jwtTokenProvider.issueAccessToken(memberId, role.name())).thenReturn(accessToken);
        when(jwtTokenProvider.issueRefreshToken(memberId, role.name())).thenReturn(refreshToken);
        when(response.encodeRedirectURL("http://localhost:3000/auth")).thenReturn(encodedUrl);

        // 응답 쿠키
        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);

        // when
        successHandler.onAuthenticationSuccess(request, response, authentication);

        // then
        verify(response, times(2)).addCookie(cookieCaptor.capture());
        List<Cookie> cookies = cookieCaptor.getAllValues();

        assertThat(cookies).hasSize(2);
        Cookie accessCookie = cookies.get(0);
        Cookie refreshCookie = cookies.get(1);

        assertThat(accessCookie.getName()).isEqualTo("accessToken");
        assertThat(accessCookie.getValue()).isEqualTo(accessToken);
        assertThat(accessCookie.getMaxAge()).isEqualTo(60);

        assertThat(refreshCookie.getName()).isEqualTo("refreshToken");
        assertThat(refreshCookie.getValue()).isEqualTo(refreshToken);
        assertThat(refreshCookie.getMaxAge()).isEqualTo(120);

        // 마지막에서 리다이렉트 검증
        verify(response).encodeRedirectURL("http://localhost:3000/auth");
        verify(response).sendRedirect(encodedUrl);

    }

    @Test
    @DisplayName("onAuthenticationSuccess - 인증 실패로 401 에러 반환")
    void onAuthenticationSuccess_UnauthorizedWhenPrincipalNotCustomUserDetails() throws Exception {
        // given
        when(authentication.getPrincipal()).thenReturn("올바르지않은 principal 입니다.");

        // when
        successHandler.onAuthenticationSuccess(request, response, authentication);

        // then
        verify(response, times(1)).sendError(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response, never()).addCookie(any());
        verify(response, never()).sendRedirect(anyString());
    }
}