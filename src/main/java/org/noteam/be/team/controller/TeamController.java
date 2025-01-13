package org.noteam.be.team.controller;


import lombok.RequiredArgsConstructor;
import org.noteam.be.team.dto.TeamRegisterDto;
import org.noteam.be.team.dto.TeamResponseDto;
import org.noteam.be.team.service.TeamServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController("/api/team")
@RequiredArgsConstructor
public class TeamController {


    private final TeamServiceImpl teamServiceImpl;


    // 메서드 기능: 등록Dto를 매개변수로 받아서, 엔티티로 변환후 Team DB에 저장한다
    // 반환: 엔티티를 fromEntity 메서드를통해 응답Dto로 바꾼후 반환한다
    @PostMapping("/post")
    public TeamResponseDto createTeam(TeamRegisterDto dto) {

        return teamServiceImpl.createTeamByDto(dto);
    }



    // 메서드 기능: id로 해당 team 엔티티를 찾는다
    // 조건: delete false 인것만 찾는다
    // 예외: 존재하지않으면 예외를 던진다
    // 반환: 엔티티를 fromEntity 메서드를통해 응답Dto로 바꾼후 반환한다
    @GetMapping("/get")
    public TeamResponseDto getTeamById(Long id) {

        return teamServiceImpl.getTeamByIdWithResponse(id);

    }


    // 메서드 기능: 이곳의 내부 메서드를 이용하여 team 엔티티를 찾는다.
    // 그리고 setter를 이용하여 Dto에 저장된 팀명으로 바꾼뒤 다시 저장한다. 등록할때 입력받는 변수가 같아서 그대로 RegisterDto를 사용했다.
    // 예외: 내부메서드에서 예외처리된다
    // 반환: 엔티티를 fromEntity 메서드를통해 응답Dto로 바꾼후 반환한다
    @PutMapping("/put")
    public TeamResponseDto updateTeam(Long id, TeamRegisterDto dto) {

        return teamServiceImpl.updateTeam(id, dto);

    }



    // 메서드 기능: id로 team 엔티티를 찾아서, setter로 소프트 딜리튼 처리한다
    // 예외: 해당 엔티티가 존재하지않으면 예외를 던진다
    @DeleteMapping("/delete")
    public void deleteTeam(Long id) {

        teamServiceImpl.deleteTeamById(id);

    }



}
