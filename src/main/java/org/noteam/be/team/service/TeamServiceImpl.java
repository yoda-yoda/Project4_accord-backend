package org.noteam.be.team.service;


import lombok.RequiredArgsConstructor;
import org.noteam.be.team.domain.Team;
import org.noteam.be.team.dto.TeamRegisterDto;
import org.noteam.be.team.dto.TeamResponseDto;
import org.noteam.be.team.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamServiceImpl {


    private final TeamRepository teamRepository;


    // 메서드 기능: 등록Dto를 매개변수로 받아서, 엔티티로 변환후 Team DB에 저장한다
    // 반환: 엔티티를 fromEntity 메서드를통해 응답Dto로 바꾼후 반환한다
    @Transactional
    public TeamResponseDto createTeamByDto(TeamRegisterDto dto) {

        Team team = TeamRegisterDto.toEntity(dto);

        Team savedTeam = teamRepository.save(team);

        TeamResponseDto teamResponseDto = TeamResponseDto.fromEntity(savedTeam);

        return teamResponseDto;

    }



    // 메서드 기능: id로 해당 team 엔티티를 찾는다
    // 조건: delete false 인것만 찾는다
    // 예외: 존재하지않으면 예외를 던진다
    // 반환: 엔티티를 fromEntity 메서드를통해 응답Dto로 바꾼후 반환한다
    public TeamResponseDto getTeamByIdWithResponse(Long id) {

        // delete를 filter()를 활용해 false인것만 찾도록 필터링한다
        Team findTeam = teamRepository.findById(id)
                .filter(team -> !team.isDeleted()).orElseThrow();

        TeamResponseDto teamResponseDto = TeamResponseDto.fromEntity(findTeam);

        return teamResponseDto;

    }




    // 메서드 기능: id로 해당 team 엔티티를 찾는다
    // 조건: delete false 인것만 찾는다
    // 예외: 존재하지않으면 예외를 던진다
    // 반환: 해당 Team을 반환한다.
    public Team getTeamById(Long id) {

        // delete를 filter()를 활용해 false인것만 찾도록 필터링한다
        Team findTeam = teamRepository.findById(id)
                .filter(team -> !team.isDeleted()).orElseThrow();

        return findTeam;

    }




    // 메서드 기능: 이곳의 내부 메서드를 이용하여 team 엔티티를 찾는다.
    // 그리고 setter를 이용하여 Dto에 저장된 팀명으로 바꾼뒤 다시 저장한다. 등록할때 입력받는 변수가 같아서 그대로 RegisterDto를 사용했다.
    // 예외: 내부메서드에서 예외처리된다
    // 반환: 엔티티를 fromEntity 메서드를통해 응답Dto로 바꾼후 반환한다
    @Transactional
    public TeamResponseDto updateTeam(Long id, TeamRegisterDto dto) {

        Team team = getTeamById(id);

        team.setTeamName(dto.getTeamName());

        Team savedTeam = teamRepository.save(team);

        TeamResponseDto teamResponseDto = TeamResponseDto.fromEntity(savedTeam);

        return teamResponseDto;
    }



    // 메서드 기능: id로 team 엔티티를 찾아서, setter로 소프트 딜리튼 처리한다
    // 예외: 해당 엔티티가 존재하지않으면 예외를 던진다
    @Transactional
    public void deleteTeamById(Long id) {

        Team findTeam = teamRepository.findById(id)
                .orElseThrow();

        findTeam.setDeleted(true);

        teamRepository.save(findTeam);

    }




}
