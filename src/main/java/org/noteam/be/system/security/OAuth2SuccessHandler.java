package org.noteam.be.system.security;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.member.dto.CustomUserDetails;
import org.noteam.be.member.service.JwtTokenProvider;
import org.noteam.be.system.config.JwtConfiguration;
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

    // application.yml 에 프론트 리디렉션 경로 설정 현재는 비활성화
    // @Value("${custom.frontend.redirect-uri}")
    private String baseRedirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // Authentication에서 principal 꺼내옴.
        if (!(authentication.getPrincipal() instanceof CustomUserDetails)) {
            log.error("[OAuth2SuccessHandler] Principal is not CustomUserDetails. Auth failed?");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        Long memberId = principal.getMemberId(); // 멤버ID
        String role = principal.getRole().name(); // member 로 나옴

        // Access / Refresh 토큰 발급
        String accessToken = jwtTokenProvider.issueAccessToken(memberId, role);
        String refreshToken = jwtTokenProvider.issueRefreshToken(memberId, role);

        // 쿠키 만료시간설정 -> application.yml에 설정된것을 가져와서 변환
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
        // cookie.setSecure(true); // HTTPS 에서만 전송
        response.addCookie(cookie);
    }
}