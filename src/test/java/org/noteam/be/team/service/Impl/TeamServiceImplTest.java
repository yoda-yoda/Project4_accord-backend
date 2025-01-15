package org.noteam.be.team.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.noteam.be.system.exception.ExceptionMessage;
import org.noteam.be.system.exception.team.TeamNotFoundException;
import org.noteam.be.team.domain.Team;
import org.noteam.be.team.dto.TeamRegisterRequest;
import org.noteam.be.team.dto.TeamResponse;
import org.noteam.be.team.repository.TeamRepository;
import org.noteam.be.team.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@Rollback(false)
@Slf4j
class TeamServiceImplTest {

    
    @Autowired
    TeamServiceImpl teamServiceImpl;

    TeamResponse teamResponseForGetId;
    // BeforeEach에서 만들어진 엔티티를 저장해놓고, id를 얻기위한 용도의 필드변수다.


    @BeforeEach
    @DisplayName("매 테스트마다 엔티티를 create해놓기 번거로워서 만든 BeforeEach")
    void setUp1() {

        TeamRegisterRequest teamRegisterRequest = TeamRegisterRequest.builder()
                .teamName("테스트팀네임1")
                .build();

        teamResponseForGetId = teamServiceImpl.createTeamByDto(teamRegisterRequest);


    }


    @AfterEach
    @DisplayName("BeforeEach로 인한 결과를 다시 없애주기위한 AfterEach 메서드")
    void afterEach1() {
        teamServiceImpl.hardDeleteAll();
    }



    @Test
    @DisplayName("teamServiceImpl의 createTeamByDto 메서드테스트 - RegisterDto 만드는것부터 진행함")
    void createTeamByDto_method_test1() throws Exception {

          // given
        TeamRegisterRequest teamRegisterRequest = TeamRegisterRequest.builder()
                .teamName("테스트팀네임2")
                .build();


    	  // when
        TeamResponse teamByDto = teamServiceImpl.createTeamByDto(teamRegisterRequest);


        // then
        assertThat(teamByDto.getTeamName()).isEqualTo("테스트팀네임2");

        log.info("teamByDto.getCreatedAt() = {}", teamByDto.getCreatedAt());
        log.info("teamByDto.getUpdatedAt() = {}", teamByDto.getUpdatedAt());

        // 결과1 ok: 위에 assertThat도 통과, log도 확인결과 잘 통과함
        // 결과2 ok: db 직접 확인 결과 테이블에 엔티티도 잘 생성됨
    }


    @Test
    @DisplayName("teamServiceImpl의 getTeamByIdWithResponse 메서드테스트1")
    void getTeamByIdWithResponse_method_test1() throws Exception {

          // given
        // BeforeEach로 Team 엔티티 1개 생성됨.

    	  // when
        TeamResponse teamByIdWithResponse = teamServiceImpl.getTeamByIdWithResponse(teamResponseForGetId.getId());

        // then
        assertThat(teamByIdWithResponse.getTeamName()).isEqualTo("테스트팀네임1");

        log.info("teamByIdWithResponse.getCreatedAt() = {}", teamByIdWithResponse.getCreatedAt());
        log.info("teamByIdWithResponse.getUpdatedAt() = {}", teamByIdWithResponse.getUpdatedAt());

        // 결과:ok
    }


    @Test
    @DisplayName("teamServiceImpl의 getTeamById 메서드테스트1")
    void getTeamById_method_test1() throws Exception {

    		// given
        // BeforeEach로 Team 엔티티 1개 생성됨.

    	  // when
        Team team = teamServiceImpl.getTeamById(teamResponseForGetId.getId());

        // then
        assertThat(team.getTeamName()).isEqualTo("테스트팀네임1");
        assertThat(team.isDeleted()).isEqualTo(false);
        log.info("team.getCreatedAt() = {}", team.getCreatedAt());
        log.info("team.getUpdatedAt() = {}", team.getUpdatedAt());

        // 결과:ok

    }


    @Test
    @DisplayName("teamServiceImpl의 getAllTeam 메서드테스트1")
    void getAllTeam_method_test1() throws Exception {

    		// given
        // BeforeEach로 Team 엔티티 1개 생성됨

    	  // when
        List<Team> allTeam = teamServiceImpl.getAllTeam();

        // then
        assertThat(allTeam.size()).isEqualTo(1);
        assertThat(!allTeam.isEmpty()).isEqualTo(true);

        Team team = allTeam.get(0);
        assertThat(team.getTeamName()).isEqualTo("테스트팀네임1");
        assertThat(team.isDeleted()).isEqualTo(false);
        log.info("team.getCreatedAt() = {}", team.getCreatedAt());
        log.info("team.getUpdatedAt() = {}", team.getUpdatedAt());

    }


    @Test
    @DisplayName("teamServiceImpl의 getAllTeam 메서드테스트2 - DB에 아무것도 없을때 ")
    void getAllTeam_method_test2() throws Exception {

        // given
        // (1) BeforeEach로 Team 엔티티 1개 생성됨.

        teamServiceImpl.hardDeleteAll();
        // (2) 그리고 테스트 조건을위해 그 엔티티를 삭제하였다.

        // when
        List<Team> allTeam = teamServiceImpl.getAllTeam();

        // then
        assertThat(allTeam.size()).isEqualTo(0);
        assertThat(allTeam.isEmpty()).isEqualTo(true);

        log.info("allTeam.toString() = {}", allTeam.toString()); // [] 여야함

        // 결과: ok

    }


    @Test
    @DisplayName("teamServiceImpl의 updateTeamByDto 메서드테스트1 - 참고로 입력 필드변수가 그대로이기때문에 RegisterDto를 그대로 UpdateDto로 사용했다.")
    void updateTeamByDto_method_test1() throws Exception {

        // given
        // (1) BeforeEach로 Team 엔티티 1개 생성됨.

        TeamRegisterRequest teamRegisterRequest = TeamRegisterRequest.builder()
                .teamName("업데이트한 팀이름")
                .build();


        // when
        TeamResponse teamResponse = teamServiceImpl.updateTeamByDto(teamResponseForGetId.getId(), teamRegisterRequest);


        // then
        assertThat(teamResponse.getTeamName()).isEqualTo("업데이트한 팀이름");

        log.info("teamResponse.getCreatedAt() = {}", teamResponse.getCreatedAt());
        log.info("teamResponse.getUpdatedAt() = {}", teamResponse.getUpdatedAt()); // 바뀌어야함


        // 결과: ok 디비에서 updated_at이 0.2초 차이긴하지만 바뀐것도 확인함

    }



    @Test
    @DisplayName("teamServiceImpl의 deleteTeamById 메서드테스트1")
    void deleteTeamById_method_test1() throws Exception {

        // given
        // (1) BeforeEach로 Team 엔티티 1개 생성됨.


        // when
        teamServiceImpl.deleteTeamById(teamResponseForGetId.getId());
        Team findTeam = teamServiceImpl.findById(teamResponseForGetId.getId())
                .orElseThrow( () -> new TeamNotFoundException(ExceptionMessage.TEAM_NOT_FOUND_ERROR) );


        // then
        assertThat(findTeam.isDeleted()).isEqualTo(true);

        // 결과: ok  디비에서도 확인함

    }









}