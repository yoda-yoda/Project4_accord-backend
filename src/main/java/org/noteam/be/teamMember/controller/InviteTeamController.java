package org.noteam.be.teamMember.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.noteam.be.system.util.SecurityUtil;
import org.noteam.be.teamMember.service.SendEmailService;
import org.noteam.be.teamMember.service.TeamInviteResponseService;
import org.noteam.be.teamMember.dto.InviteMemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Tag(name = "팀관리", description = "협업 팀 관리 API")
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class InviteTeamController {

    private final SendEmailService sendEmailService;
    private final TeamInviteResponseService teamInviteResponseService;


    //inviter는 나중에 로그인 정보에서 가입자의 주소를 가져온다
    @Operation(summary = "팀 초대", description = "해당 회원의 이메일로 팀 초대장 발송")
    @ResponseBody
    @GetMapping("/teams/members/{teamId}/{memberId}")
    public ResponseEntity<InviteMemberResponse> sendInviteMessage(@PathVariable Long teamId, @PathVariable Long memberId) {
        InviteMemberResponse result = sendEmailService.sendInviteEmail(teamId, memberId);
        return ResponseEntity.ok(result);
    }


    // 도착한 이메일을 클릭하면 팀 멤버에 저장된다.
    @Operation(summary = "팀 수락", description = "초대장을 받은 회원이 수락 후 팀원으로 등록됨")
    @ResponseBody
    @PostMapping("/teams/members/{teamId}")
    public ResponseEntity<InviteMemberResponse> acceptInvite(@PathVariable Long teamId) {

        InviteMemberResponse result = teamInviteResponseService.AcceptTeamInvite(teamId, SecurityUtil.getCurrentMemberId());

        return ResponseEntity.ok(result);

    }
}

