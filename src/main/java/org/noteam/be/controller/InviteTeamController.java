package org.noteam.be.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.noteam.be.dto.InviteMemberResponce;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.noteam.be.Email.Service.Impl.TeamInviteResponseServiceImpl;
import org.noteam.be.Email.Service.Impl.SendEmailServiceImpl;


@Slf4j
@Controller
@RequiredArgsConstructor
//@RequestMapping("/api/members")
public class InviteTeamController {

    private final SendEmailServiceImpl sendEmailServiceImpl;
    private final TeamInviteResponseServiceImpl teamInviteResponseServiceImpl;


    //inviter는 나중에 로그인 정보에서 가입자의 주소를 가져온다
    @ResponseBody
    @GetMapping("/teams/{teamId}/{memberId}")
    public ResponseEntity<InviteMemberResponce> sendInviteMessage(@PathVariable Long teamId, @PathVariable Long memberId) {
        InviteMemberResponce result = sendEmailServiceImpl.sendInviteEmail("석환", teamId, memberId);
        return ResponseEntity.ok(result);
    }


    // 도착한 이메일을 클릭하면 팀 멤버에 저장된다.
    @ResponseBody
    @PostMapping("/teams/{teamId}/{memberId}")
    public ResponseEntity<InviteMemberResponce> acceptInvite(@PathVariable Long teamId, @PathVariable Long memberId) {

        InviteMemberResponce result = teamInviteResponseServiceImpl.AcceptTeamInvite(teamId, memberId);

//         성공하면 success,
//         실패하면 failed ,
//         이미 팀에 포함 되어있다면 already there 반환
        return ResponseEntity.ok(result);

    }


}

