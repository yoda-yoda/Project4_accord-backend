package org.noteam.be.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.member.dto.CustomUserDetails;
import org.noteam.be.member.dto.NicknameUpdateRequest;
import org.noteam.be.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberRestController {

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

}
