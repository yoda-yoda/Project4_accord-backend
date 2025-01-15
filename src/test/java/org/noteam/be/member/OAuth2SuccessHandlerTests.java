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
import org.noteam.be.member.domain.Role;
import org.noteam.be.member.dto.CustomUserDetails;
import org.noteam.be.member.service.JwtTokenProvider;
import org.noteam.be.system.config.JwtConfiguration;
import org.noteam.be.system.security.OAuth2SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
//@TestPropertySource(locations = "classpath:applicationTest.yml")
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

    @InjectMocks
    private OAuth2SuccessHandler successHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        JwtConfiguration.Validation validation = new JwtConfiguration.Validation();
        validation.setAccess(60000L); // 1 minute
        validation.setRefresh(120000L); // 2 minutes
        when(jwtConfiguration.getValidation()).thenReturn(validation);

        // Mock JWT configuration 셋업
        JwtConfiguration.Secret secret = new JwtConfiguration.Secret();
        secret.setAppKey("your-256-bit-secret-key-for-testing-purposes-only");
        when(jwtConfiguration.getSecret()).thenReturn(secret);

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

        when(authentication.getPrincipal()).thenReturn(principal);
        when(jwtTokenProvider.issueAccessToken(memberId, role.name())).thenReturn(accessToken);
        when(jwtTokenProvider.issueRefreshToken(memberId, role.name())).thenReturn(refreshToken);

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

        verify(response, times(1)).sendRedirect(anyString());
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