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

        // Refresh 토큰만 DB 검증 후 블랙리스트에 추가
        Optional<RefreshToken> refreshTokenOptional = tokenRepository.findValidRefTokenByToken(refreshTokenValue);
        if (refreshTokenOptional.isPresent()) {
            tokenRepository.appendBlackList(refreshTokenOptional.get());
            log.info("로그아웃 처리 완료 - Refresh 토큰 블랙리스트 등록됨. refresh={}", refreshTokenValue);
        } else {
            // 이미 블랙리스트이거나, 존재하지 않는 토큰
            log.info("로그아웃 처리 완료 - Refresh 토큰이 DB에 없거나 이미 블랙리스트에 있음. refresh={}", refreshTokenValue);
        }

//        // Access Token 은 블랙리스트 처리하지않도록 변경. db에 저장도 하지않고 만료시간이 짧기때문에 로그아웃시 자연만료되도록함.
//        tokenRepository.appendBlackList(RefreshToken.builder()
//                .refreshToken(accessToken)
//                .build()
//        );
//        tokenRepository.appendBlackList(RefreshToken.builder()
//                .refreshToken(refreshTokenValue)
//                .build()
//        );

    }
}
