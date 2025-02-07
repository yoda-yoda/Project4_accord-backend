package org.noteam.be.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.noteam.be.member.dto.CustomUserDetails;
import org.noteam.be.member.service.FacadeMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class FacadeMemberController {

    private final FacadeMemberService facadeMemberService;

    @Operation(summary = "회원정보 조회", description = "회원정보 memberId로 조회")
    @GetMapping("/userinfos")
    public ResponseEntity<?> getUserInfos(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Unauthorized");
        }
        return ResponseEntity.ok(Map.of("memberInfo", facadeMemberService.getFacadeMemberProfileResponse(userDetails.getMemberId())));
    }
}
