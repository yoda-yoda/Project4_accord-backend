package org.noteam.be.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.RefreshToken;
import org.noteam.be.member.domain.Role;
import org.noteam.be.member.dto.TokenBody;
import org.noteam.be.member.repository.TokenRepository;
import org.noteam.be.member.service.AuthServiceImpl;
import org.noteam.be.member.service.JwtTokenProvider;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthServiceImplTests {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Access Token 재발급 성공")
    void reissueAccessTokenSuccessfully() {
        // given
        String validRefreshToken = "validRefreshToken";
        TokenBody tokenBody = new TokenBody(1L, "ROLE_USER",false);

        // Mock Member 객체 생성
        Member mockMember = Member.builder()
                .email("testuser@example.com")
                .nickname("TestUser")
                .role(Role.MEMBER)
                .build();

        // Mock 동작 설정
        when(jwtTokenProvider.validate(validRefreshToken)).thenReturn(true);
        when(tokenRepository.findValidRefTokenByToken(validRefreshToken))
                .thenReturn(Optional.of(RefreshToken.builder()
                        .refreshToken(validRefreshToken)
                        .member(mockMember)
                        .build()));
        when(jwtTokenProvider.parseJwt(validRefreshToken)).thenReturn(tokenBody);
        when(jwtTokenProvider.issueAccessToken(tokenBody.getMemberId(), tokenBody.getRole()))
                .thenReturn("newAccessToken");

        // when
        String newAccessToken = authService.reissueAccessToken(validRefreshToken);

        // then
        assertThat(newAccessToken).isEqualTo("newAccessToken");
        verify(jwtTokenProvider, times(1)).validate(validRefreshToken);
        verify(tokenRepository, times(1)).findValidRefTokenByToken(validRefreshToken);
        verify(jwtTokenProvider, times(1)).parseJwt(validRefreshToken);
    }

    @Test
    @DisplayName("Access Token 재발급 실패 - 잘못된 Refresh Token 일때")
    void reissueAccessTokenFailsWithInvalidToken() {
        // given
        String invalidRefreshToken = "invalidRefreshToken";
        when(jwtTokenProvider.validate(invalidRefreshToken)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.reissueAccessToken(invalidRefreshToken))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("올바르지 않은 리프레쉬 토큰입니다.");

        verify(jwtTokenProvider, times(1)).validate(invalidRefreshToken);
        verify(tokenRepository, never()).findValidRefTokenByToken(anyString());
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logoutSuccessfully() {
        // given
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        Authentication authentication = mock(Authentication.class);

        RefreshToken existingRefToken = RefreshToken.builder()
                .refreshToken(refreshToken)
                .build();

        when(tokenRepository.findValidRefTokenByToken(refreshToken))
                .thenReturn(Optional.of(existingRefToken));

        // when
        authService.logout(accessToken, refreshToken, authentication);

        // then
        verify(tokenRepository, times(1))
                .appendBlackList(argThat(rt -> rt.getRefreshToken().equals(refreshToken)));
        // Refresh 토큰만  블랙리스트 등록
    }

    @Test
    @DisplayName("로그아웃 실패 - 인증 정보 없음")
    void logoutFailsWithoutAuthentication() {
        // given
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        // when & then
        assertThatThrownBy(() -> authService
                .logout(accessToken, refreshToken, null))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("인가정보가 존재하지 않습니다.");

        verify(tokenRepository, never()).appendBlackList(any(RefreshToken.class));
    }
}
