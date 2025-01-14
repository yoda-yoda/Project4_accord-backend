package org.noteam.be.team.controller;


import lombok.RequiredArgsConstructor;
import org.noteam.be.system.response.ResponseCode;
import org.noteam.be.system.response.ResponseData;
import org.noteam.be.team.dto.TeamRegisterRequest;
import org.noteam.be.team.dto.TeamResponse;
import org.noteam.be.team.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;


    // 메서드 기능: 등록Dto를 매개변수로 받아서, 엔티티로 변환후 Team DB에 저장한다
    // 반환: 엔티티를 fromEntity 메서드를통해 응답Dto로 바꾼후 반환한다
    @PostMapping
    public ResponseEntity<ResponseData<TeamResponse>> createTeam(TeamRegisterRequest dto) {

        TeamResponse teamByDto = teamService.createTeamByDto(dto);

        return ResponseData.toResponseEntity(ResponseCode.POST_TEAM_SUCCESS, teamByDto);

    }



    // 메서드 기능: id로 해당 team 엔티티를 찾는다
    // 조건: delete false 인것만 찾는다
    // 예외: 존재하지않으면 예외를 던진다
    // 반환: 엔티티를 fromEntity 메서드를통해 응답Dto로 바꾼후 반환한다
    @GetMapping("/{teamId}")
    public ResponseEntity<ResponseData<TeamResponse>> getTeamById(@PathVariable Long teamId) {

        TeamResponse teamByIdWithResponse = teamService.getTeamByIdWithResponse(teamId);

        return ResponseData.toResponseEntity(ResponseCode.GET_TEAM_SUCCESS, teamByIdWithResponse);

    }


    // 메서드 기능: 이곳의 내부 메서드를 이용하여 team 엔티티를 찾는다.
    // 그리고 setter를 이용하여 Dto에 저장된 팀명으로 바꾼뒤 다시 저장한다. 등록할때 입력받는 변수가 같아서 그대로 RegisterDto를 사용했다.
    // 예외: 내부메서드에서 예외처리된다
    // 반환: 엔티티를 fromEntity 메서드를통해 응답Dto로 바꾼후 반환한다
    @PutMapping("/{teamId}")
    public ResponseEntity<ResponseData<TeamResponse>> updateTeamByDto(@PathVariable Long teamId, TeamRegisterRequest dto) {

        TeamResponse teamResponse = teamService.updateTeamByDto(teamId, dto);

        return ResponseData.toResponseEntity(ResponseCode.UPDATE_TEAM_SUCCESS, teamResponse);

    }



    // 메서드 기능: id로 team 엔티티를 찾아서, setter로 소프트 딜리트 처리한다
    // 예외: 해당 엔티티가 존재하지않으면 예외를 던진다
    @DeleteMapping("/{teamId}")
    public ResponseEntity<ResponseData> deleteTeam(@PathVariable Long teamId) {

        teamService.deleteTeamById(teamId);

        return ResponseData.toResponseEntity(ResponseCode.DELETE_TEAM_SUCCESS);
    }



}
