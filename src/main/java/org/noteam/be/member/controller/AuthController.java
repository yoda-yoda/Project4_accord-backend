package org.noteam.be.member.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.member.dto.CustomUserDetails;
import org.noteam.be.member.service.MemberService;
import org.springframework.security.core.Authentication;
import org.noteam.be.member.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "인증 관리", description = "로그인/로그아웃 등 회원 인증 관리 API")
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final AuthService authService;

    @Operation(summary = "토큰 재발급", description = "리프레쉬 토큰을 통한 토큰 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<?> reissueAccessToken(
            @RequestHeader("Refresh-Token") String refreshTokenValue
    ) {
            String newAccessToken = authService.reissueAccessToken(refreshTokenValue);
            return ResponseEntity.ok(newAccessToken);
    }

    @Operation(summary = "로그아웃", description = "로그아웃 진행하며 토큰도 블랙리스트 처리")
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

    @Operation(summary = "회원탈퇴", description = "회원 상태값 DELETED로 처리를 통한 회원탈퇴")
    @DeleteMapping
    public ResponseEntity<?> deleteMember(
            @AuthenticationPrincipal CustomUserDetails userDetails){

        Long memberId = userDetails.getMemberId();

        memberService.deleteMember(memberId);
        return ResponseEntity.ok("탈퇴 완료.");
    }

}