package org.noteam.be.system.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.dto.CustomUserDetails;
import org.noteam.be.member.dto.KeyPair;
import org.noteam.be.member.repository.MemberRepository;
import org.noteam.be.member.service.JwtTokenProvider;
import org.noteam.be.system.config.JwtConfiguration;
import org.noteam.be.system.exception.ExceptionMessage;
import org.noteam.be.system.exception.member.MemberNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtConfiguration jwtConfiguration;
    private final MemberRepository memberRepository;

    @Value("${custom.frontend.redirect-uri}")
    private String baseRedirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // Authentication에서 principal 꺼내옴.
        if (!(authentication.getPrincipal() instanceof CustomUserDetails)) {
            log.error("[OAuth2SuccessHandler] 인증 실패.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        Long memberId = principal.getMemberId(); // 멤버ID
        String role = principal.getRole().name(); // member 로 나옴

        // 이미 존재하는 회원 조회
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException(ExceptionMessage.MemberAuth.MEMBER_NOT_FOUND_EXCEPTION));

        // Refresh 토큰을 DB에 저장 + Access/Refresh 토큰 발급
        KeyPair keyPair = jwtTokenProvider.generateKeyPair(member);
        String accessToken = keyPair.getAccessToken();
        String refreshToken = keyPair.getRefreshToken();

        //쿠키 만료시간설정 -> application.yml에 설정된것을 가져와서 변환
        int accessCookieMaxAge = (int) (jwtConfiguration.getValidation().getAccess() / 1000);
        int refreshCookieMaxAge = (int) (jwtConfiguration.getValidation().getRefresh() / 1000);

        // 쿠키 설정
        addCookie(response, "accessToken", accessToken, accessCookieMaxAge);
        addCookie(response, "refreshToken", refreshToken, refreshCookieMaxAge);

        // 리다이렉트 토큰이 url
        getRedirectStrategy().sendRedirect(request, response, baseRedirectUri);
    }

    private void addCookie(HttpServletResponse response,
                           String name,
                           String value,
                           int maxAgeSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAgeSeconds);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // HTTPS 에서만 전송
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }

}