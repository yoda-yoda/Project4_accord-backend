package org.noteam.be.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.member.dto.CustomUserDetails;
import org.noteam.be.member.dto.NicknameUpdateRequest;
import org.noteam.be.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "회원정보관리", description = "닉네임변경 및 회원정보 조회 API")
@Slf4j
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @Operation(summary = "닉네임 변경", description = "닉네임 변경")
    @PatchMapping("/nicknames")
    public ResponseEntity<?> updateNickname(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody NicknameUpdateRequest request
    ) {

        Long memberId = userDetails.getMemberId();

        memberService.updateNickname(memberId, request);
        return ResponseEntity.ok("닉네임 변경 완료.");

    }

    @Operation(summary = "회원정보 조회", description = "회원 닉네임 조회")
    @GetMapping("/profiles")
    public ResponseEntity<?> getProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Unauthorized");
        }

        return ResponseEntity.ok(Map.of("nickname", userDetails.getNickname()));

    }

}
