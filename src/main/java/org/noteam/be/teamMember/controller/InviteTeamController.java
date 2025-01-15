package org.noteam.be.teamMember.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.noteam.be.teamMember.service.SendEmailService;
import org.noteam.be.teamMember.service.TeamInviteResponseService;
import org.noteam.be.teamMember.dto.InviteMemberResponce;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
@RequiredArgsConstructor
//@RequestMapping("/api/members")
public class InviteTeamController {

    private final SendEmailService sendEmailService;
    private final TeamInviteResponseService teamInviteResponseService;


    //inviter는 나중에 로그인 정보에서 가입자의 주소를 가져온다
    @ResponseBody
    @GetMapping("/teams/{teamId}/{memberId}")
    public ResponseEntity<InviteMemberResponce> sendInviteMessage(@PathVariable Long teamId, @PathVariable Long memberId) {
        InviteMemberResponce result = sendEmailService.sendInviteEmail("석환", teamId, memberId);
        return ResponseEntity.ok(result);
    }


    // 도착한 이메일을 클릭하면 팀 멤버에 저장된다.
    @ResponseBody
    @PostMapping("/teams/{teamId}/{memberId}")
    public ResponseEntity<InviteMemberResponce> acceptInvite(@PathVariable Long teamId, @PathVariable Long memberId) {

        InviteMemberResponce result = teamInviteResponseService.AcceptTeamInvite(teamId, memberId);

        return ResponseEntity.ok(result);

    }


}

