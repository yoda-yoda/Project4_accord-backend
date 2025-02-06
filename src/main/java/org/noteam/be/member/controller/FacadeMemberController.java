package org.noteam.be.member.controller;

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

    @GetMapping("/userinfos")
    public ResponseEntity<?> getUserInfos(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Unauthorized");
        }
        return ResponseEntity.ok(Map.of("memberInfo", facadeMemberService.getFacadeMemberProfileResponse(userDetails.getMemberId())));
    }
}
