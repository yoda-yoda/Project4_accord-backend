package org.noteam.be.member.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.RefreshToken;
import org.noteam.be.member.dto.KeyPair;
import org.noteam.be.member.dto.TokenBody;
import org.noteam.be.member.repository.TokenRepository;
import org.noteam.be.system.config.JwtConfiguration;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtConfiguration configuration;
    private final TokenRepository refreshTokenRepositoryAdapter;

    // Access Tokem, Refresh Token 발급
    public KeyPair generateKeyPair(Member member) {
        String accessToken = issueAccessToken(member.getMemberId(), member.getRole().name());
        String refreshToken = issueRefreshToken(member.getMemberId(), member.getRole().name());

        // DB에 RefreshToken 저장
        refreshTokenRepositoryAdapter.save(member, refreshToken);

        return KeyPair.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 토큰발급 프로세스 (generateKeyPair 에서 생성됨)
    public String issueAccessToken(Long id, String role) {
        return issue(id, role, configuration.getValidation().getAccess());
    }
    // 토큰발급 프로세스 (generateKeyPair 에서 생성됨)
    public String issueRefreshToken(Long id, String role) {
        return issue(id, role, configuration.getValidation().getRefresh());
    }

    // memberId, role, 만료시간 -> JWT 생성
    private String issue(Long memberId, String role, Long validTime) {
        return Jwts.builder()
                .subject(memberId.toString())
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + validTime))
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(configuration.getSecret().getAppKey().getBytes());
    }

    public boolean validate(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch ( JwtException e ) {
            log.info("JWT 토큰이 잘못되었습니다. msg = {}", e.getMessage());
            log.info("토큰 : {}", token);
        } catch ( IllegalArgumentException e ) {
            log.info("JWT 토큰이 비어있습니다. = {}", e.getMessage());
        } catch ( Exception e ) {
            log.error("토큰 검증 중 에러가 발생하였습니다. err msg = {}", e.getMessage());
        }
        return false;
    }

    // 토큰 파싱해서 TokenBody 오브젝트 생성
    public TokenBody parseJwt(String token) {
        Jws<Claims> parsed = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token);

        String memberId = parsed.getPayload().getSubject();
        Object role = parsed.getPayload().get("role");

        return new TokenBody(Long.parseLong(memberId), role.toString());
    }

    //DB에서 해당 멤버의 유효한 RefreshToken이 있는지 확인
    public RefreshToken validateRefreshToken(Long memberId) {
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepositoryAdapter.findValidRefTokenByMemberId(memberId);
        return refreshTokenOptional.orElse(null);
    }
}