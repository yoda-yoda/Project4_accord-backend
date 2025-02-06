package org.noteam.be.teamMember.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "팀관리", description = "협업 팀 관리 API")
@RestController
@RequestMapping("/api/members/team-members")
@RequiredArgsConstructor
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

    @Operation(summary = "팀원 초대를 위한 회원조회", description = "해당 팀에 속하지 않은 회원을 조회")
    @GetMapping("/search")
    public ResponseEntity<ResponseData<List<MemberProfileResponse>>> searchTeamMembers(@RequestParam String query, @RequestParam Long teamId) {
        List<MemberProfileResponse> memberProfileResponseList = teamMemberService.findMembersByEmail(query, teamId);
        return ResponseData.toResponseEntity(ResponseCode.GET_USER_PROFILE_SUCCESS, memberProfileResponseList);
    }

    @Operation(summary = "팀원 불러오기", description = "현재 팀원 목록 조회")
    @GetMapping
    public ResponseEntity<ResponseData<List<MemberProfileResponse>>> getTeamMembers(@RequestParam Long teamId) {
        List<MemberProfileResponse> memberProfileResponseList = teamMemberService.findMembersByTeamId(teamId);
        return ResponseData.toResponseEntity(ResponseCode.GET_USER_PROFILE_SUCCESS, memberProfileResponseList);
    }

}
