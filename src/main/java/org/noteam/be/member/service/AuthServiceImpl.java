package org.noteam.be.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.member.domain.RefreshToken;
import org.noteam.be.member.dto.TokenBody;
import org.noteam.be.member.repository.TokenRepository;
import org.noteam.be.system.exception.ExceptionMessage;
import org.noteam.be.system.exception.member.EmptyRefreshToken;
import org.noteam.be.system.exception.member.ExistingAuthenticationIsNull;
import org.noteam.be.system.exception.member.InvalidRefreshTokenProvided;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;

    @Override
    public String reissueAccessToken(String refreshTokenValue) {

        // 리프레시 토큰 검증
        if (!jwtTokenProvider.validate(refreshTokenValue)) {
            throw new InvalidRefreshTokenProvided(ExceptionMessage.MemberAuth.INVALID_REFRESH_TOKEN_PROVIDED);
        }

        // DB에서 토큰 확인
        Optional<RefreshToken> tokenOpt = tokenRepository.findValidRefTokenByToken(refreshTokenValue);
        if (tokenOpt.isEmpty()) {
            throw new EmptyRefreshToken(ExceptionMessage.MemberAuth.EMPTY_REFRESH_TOKEN);
        }

        // 토큰 파싱
        TokenBody tokenBody = jwtTokenProvider.parseJwt(refreshTokenValue);

        // 새 Access Token 발급
        return jwtTokenProvider.issueAccessToken(tokenBody.getMemberId(), tokenBody.getRole());

    }

    @Override
    public void logout(String accessToken, String refreshTokenValue, Authentication authentication) {

        // authentication 객체 확인
        if (authentication == null) {
            throw new ExistingAuthenticationIsNull(ExceptionMessage.MemberAuth.EXISTING_AUTHENTICATION_IS_NULL);
        }

        // Access Token, Refresh Token 블랙리스트 등록
        tokenRepository.appendBlackList(RefreshToken.builder()
                .refreshToken(accessToken)
                .build()
        );
        tokenRepository.appendBlackList(RefreshToken.builder()
                .refreshToken(refreshTokenValue)
                .build()
        );

        log.info("로그아웃 처리 완료 - access={}, refresh={}", accessToken, refreshTokenValue);

    }
}
