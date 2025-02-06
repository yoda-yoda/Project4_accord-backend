package org.noteam.be.member.controller;

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

@Slf4j
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/nicknames")
    public ResponseEntity<?> updateNickname(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody NicknameUpdateRequest request
    ) {

        Long memberId = userDetails.getMemberId();

        memberService.updateNickname(memberId, request);
        return ResponseEntity.ok("닉네임 변경 완료.");

    }

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
