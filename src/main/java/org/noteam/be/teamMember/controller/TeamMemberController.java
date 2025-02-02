package org.noteam.be.teamMember.controller;

import lombok.RequiredArgsConstructor;
import org.noteam.be.member.dto.MemberProfileResponse;
import org.noteam.be.system.response.ResponseCode;
import org.noteam.be.system.response.ResponseData;
import org.noteam.be.teamMember.service.TeamMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/members/team-members")
@RequiredArgsConstructor
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

    @GetMapping("/search")
    public ResponseEntity<ResponseData<List<MemberProfileResponse>>> searchTeamMembers(@RequestParam String query, @RequestParam Long teamId) {
        List<MemberProfileResponse> memberProfileResponseList = teamMemberService.findMembersByEmail(query, teamId);
        return ResponseData.toResponseEntity(ResponseCode.GET_USER_PROFILE_SUCCESS, memberProfileResponseList);
    }

    @GetMapping
    public ResponseEntity<ResponseData<List<MemberProfileResponse>>> getTeamMembers(@RequestParam Long teamId) {
        List<MemberProfileResponse> memberProfileResponseList = teamMemberService.findMembersByTeamId(teamId);
        return ResponseData.toResponseEntity(ResponseCode.GET_USER_PROFILE_SUCCESS, memberProfileResponseList);
    }

}
