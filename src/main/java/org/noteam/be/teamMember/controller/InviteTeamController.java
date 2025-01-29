package org.noteam.be.teamMember.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.noteam.be.system.util.SecurityUtil;
import org.noteam.be.teamMember.service.SendEmailService;
import org.noteam.be.teamMember.service.TeamInviteResponseService;
import org.noteam.be.teamMember.dto.InviteMemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class InviteTeamController {

    private final SendEmailService sendEmailService;
    private final TeamInviteResponseService teamInviteResponseService;


    //inviter는 나중에 로그인 정보에서 가입자의 주소를 가져온다
    @ResponseBody
    @GetMapping("/teams/members/{teamId}/{memberId}")
    public ResponseEntity<InviteMemberResponse> sendInviteMessage(@PathVariable Long teamId, @PathVariable Long memberId) {
        InviteMemberResponse result = sendEmailService.sendInviteEmail(teamId, memberId);
        return ResponseEntity.ok(result);
    }


    // 도착한 이메일을 클릭하면 팀 멤버에 저장된다.
    @ResponseBody
    @PostMapping("/teams/members/{teamId}")
    public ResponseEntity<InviteMemberResponse> acceptInvite(@PathVariable Long teamId) {

        InviteMemberResponse result = teamInviteResponseService.AcceptTeamInvite(teamId, SecurityUtil.getCurrentMemberId());

        return ResponseEntity.ok(result);

    }
}

