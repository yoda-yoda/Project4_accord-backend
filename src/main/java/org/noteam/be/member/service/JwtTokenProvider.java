package org.noteam.be.member.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.RefreshToken;
import org.noteam.be.member.dto.KeyPair;
import org.noteam.be.member.dto.TokenBody;
import org.noteam.be.member.repository.TokenRepository;
import org.noteam.be.system.config.JwtConfiguration;
import org.noteam.be.system.exception.ExceptionMessage;
import org.noteam.be.system.exception.jwt.InvalidTokenFormatException;
import org.noteam.be.system.exception.jwt.KidExtractionException;
import org.noteam.be.system.exception.jwt.PublicKeyNotFoundException;
import org.noteam.be.system.exception.jwt.TokenValidationException;
import org.noteam.be.system.security.RsaKeyManager;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtConfiguration configuration;
    private final TokenRepository refreshTokenRepositoryAdapter;

    // RSA 키 매니저 주입
    private final RsaKeyManager rsaKeyManager;

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
        return issue(id, role, configuration.getValidation().getAccess(), false);
    }
    // 토큰발급 프로세스 (generateKeyPair 에서 생성됨)
    public String issueRefreshToken(Long id, String role) {
        return issue(id, role, configuration.getValidation().getRefresh(), true);
    }

    // memberId, role, 만료시간 -> JWT 생성
    private String issue(Long memberId, String role, Long validTime, boolean isRefresh) {

        RsaKeyManager.KeyHolder current = rsaKeyManager.getCurrentKey();
        PrivateKey privateKey = current.getPrivateKey();
        String kid = current.getKid();
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .header()
                .type("JWT")
                .keyId(kid)
                .and()
                .subject(String.valueOf(memberId))
                .claim("role", role)
                .claim("isRefresh", isRefresh)
                .issuedAt(new Date(now))
                .expiration(new Date(now + validTime))
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }

    // 비대칭전략으로 변경해서 사용하지않음.
//    private SecretKey getSecretKey() {
//        return Keys.hmacShaKeyFor(configuration.getSecret().getAppKey().getBytes());
//    }

    private String getKidFromToken(String token) {
        try {
            String[] chunks = token.split("\\.");
            if (chunks.length < 2) {
                throw new InvalidTokenFormatException(ExceptionMessage.Jwt.INVALID_TOKEN_FORMAT);
            }

            Base64.Decoder decoder = Base64.getUrlDecoder();
            String header = new String(decoder.decode(chunks[0]));
            JsonObject jsonHeader = JsonParser.parseString(header).getAsJsonObject();

            return jsonHeader.get("kid").getAsString();
        } catch (Exception e) {
            throw new KidExtractionException(ExceptionMessage.Jwt.KID_EXTRACTION_FAILED);
        }
    }

    // 토큰 유효성 검증 ( kid 파싱 후에 해당 publickey 를 찾아서 검증 )
    public boolean validate(String token) {
        try {
            // KID 추출
            String kid = getKidFromToken(token);
            if (kid == null) {
                throw new KidExtractionException(ExceptionMessage.Jwt.KID_EXTRACTION_FAILED);
            }

            // PUBLIC KEY 조회
            PublicKey publicKey = rsaKeyManager.getPublicKeyByKid(kid);
            if (publicKey == null) {
                throw new PublicKeyNotFoundException(ExceptionMessage.Jwt.PUBLIC_KEY_NOT_FOUND);
            }

            // 토큰 검증
            Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (KidExtractionException | PublicKeyNotFoundException e) {
            log.error("토큰 검증 실패: {}", e.getMessage());
            throw e;
        } catch (JwtException | IllegalArgumentException e) {
            throw new TokenValidationException(ExceptionMessage.Jwt.TOKEN_VALIDATION_FAILED);
        }
    }

    // 토큰 파싱해서 TokenBody 오브젝트 생성
    public TokenBody parseJwt(String token) {
        String kid = getKidFromToken(token);
        PublicKey publicKey = rsaKeyManager.getPublicKeyByKid(kid);

        Jws<Claims> parsed = Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token);

        Claims claims = parsed.getPayload();

        return new TokenBody(
                Long.parseLong(claims.getSubject()),
                claims.get("role", String.class),
                claims.get("isRefresh", Boolean.class)
        );
    }

    //DB에서 해당 멤버의 유효한 RefreshToken이 있는지 확인. 현재 사용하지않지만 추후 사용가능성있음.
    public RefreshToken validateRefreshToken(Long memberId) {
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepositoryAdapter.findValidRefTokenByMemberId(memberId);
        return refreshTokenOptional.orElse(null);
    }
}