package org.noteam.be.member.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.member.dto.CustomUserDetails;
import org.noteam.be.member.dto.NicknameUpdateRequest;
import org.noteam.be.member.service.MemberService;
import org.springframework.security.core.Authentication;
import org.noteam.be.member.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final MemberService memberService;
    private final AuthService authService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissueAccessToken(
            @RequestHeader("Refresh-Token") String refreshTokenValue
    ) {
            String newAccessToken = authService.reissueAccessToken(refreshTokenValue);
            return ResponseEntity.ok(newAccessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            Authentication authentication,
            @RequestHeader("Authorization") String bearerToken,
            @RequestHeader("Refresh-Token") String refreshTokenValue
    ) {
            log.debug("Authentication 존재 : {}", authentication);
            // Access Token 추출
            String accessToken = bearerToken.substring(7);

            authService.logout(accessToken, refreshTokenValue, authentication);
            return ResponseEntity.ok("로그아웃 완료");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteNickname(
            @AuthenticationPrincipal CustomUserDetails userDetails){

        Long memberId = userDetails.getMemberId();

        memberService.deleteMember(memberId);
        return ResponseEntity.ok("탈퇴 완료.");
    }

}