package org.noteam.be.member.service;

import org.springframework.security.core.Authentication;

public interface AuthService {

    // 새로운 엑세스 토큰 발급
    String reissueAccessToken(String refreshTokenValue);

    // 로그아웃 시 Access/Refresh 토큰 둘 다 블랙리스트 등록
    void logout(String accessToken, String refreshTokenValue, Authentication authentication);
}
