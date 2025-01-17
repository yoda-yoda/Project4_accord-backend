package org.noteam.be.system.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.member.domain.RefreshToken;
import org.noteam.be.member.dto.TokenBody;
import org.noteam.be.member.repository.TokenRepository;
import org.noteam.be.member.service.JwtTokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String ROLE_PREFIX = "ROLE_";

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;

    /*
    1. 클라이언트가 API 요청 with Authorization 헤더
    2. JwtAuthenticationFilter가 토큰 추출
    3. 토큰 검증 성공 → TokenBody에서 사용자 정보 추출
    4. SecurityCotnext에 인증 정보 설정
    5. 스프링 시큐리티가 이 정보로 권한 확인
     */

    //
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

            String token = resolveToken(request);
            processToken(token);

            filterChain.doFilter(request, response);

    }

    private void processToken(String token) {
        if (token == null) {
            log.debug("요청에 토큰이 없습니다.");
            return;
        }

        try {
            if (jwtTokenProvider.validate(token)) {
                log.debug("토큰 검증 시작 : {}", maskToken(token));

                Optional<RefreshToken> refreshTokenOpt = tokenRepository.findValidRefTokenByToken(token);
                if (refreshTokenOpt.isEmpty()) {
                    log.info("토큰이 만료되었거나 올바르지않습니다. token={}", maskToken(token));
                    return;
                }

                TokenBody tokenBody = jwtTokenProvider.parseJwt(token);
                setSecurityContext(tokenBody);
                log.debug("사용자 인증 컨텍스트 설정 완료: {}", tokenBody.getMemberId());
            }
        } catch (ExpiredJwtException e) {
            log.warn("토큰 만료됨: {}", maskToken(token));
        } catch (JwtException e) {
            log.warn("토큰 검증 실패: {}", e.getMessage());
        }
    }

    private void setSecurityContext(TokenBody tokenBody) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(ROLE_PREFIX + tokenBody.getRole());
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                tokenBody,
                null,
                //권한이 하나만 들어있기때문에 singletonList 사용
                Collections.singletonList(authority)
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    // http 요청 헤더에서 Authorization 값이 bearer 로 시작하면 내용 추출
    private String resolveToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION_HEADER))
                .filter(header -> header.startsWith(BEARER_PREFIX))
                .map(header -> header.substring(BEARER_PREFIX.length()))
                .orElse(null);
    }

    //토큰 로깅 보안을 위해 마스킹함.
    private String maskToken(String token) {
        if (token == null || token.length() < 8) {
            return "***";
        }
        return token.substring(0, 4) + "..." + token.substring(token.length() - 4);
    }

}