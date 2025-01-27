package org.noteam.be.team.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.repository.MemberRepository;
import org.noteam.be.team.domain.Team;
import org.noteam.be.team.repository.TeamRepository;
import org.noteam.be.teamMember.dto.InviteMemberResponse;
import org.noteam.be.teamMember.service.TeamInviteResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
@Slf4j
public class TeamInviteResponseServiceImplTest {

    @Autowired
    TeamInviteResponseService teamInviteResponseService;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private MemberRepository memberRepository;

    Team team;
    Member member;

    @BeforeEach
    @DisplayName("테스트 시작을 위한 ")
    void setUp() {

        member = Member.builder()
                .nickname("TestMember")
                .email("TestEmail@gmail.com")
                .build();
        memberRepository.save(member);

        team = Team.builder()
                .teamName("TestTeam")
                .build();
        teamRepository.save(team);
    }


    @Test
    @DisplayName("AcceptTeamInvite")
    void TeamInviteResponseServiceImplTest() throws Exception {

        // given
        Long teamId = team.getId();
        log.info("teamId = {}", teamId);

        Long memberId = member.getMemberId();
        log.info("memberId = {}", memberId);

        // when
        // 저장 한 값이 위에 있는 team과  member를 가지고 있는가?
        InviteMemberResponse result = teamInviteResponseService.AcceptTeamInvite(teamId, memberId);
        String message = result.getMessage();

        // then
        // TeamMember에 값이 제대로 들어갔다면 Success Add Team Member가 나온다.
        Assertions.assertThat(message).isEqualTo("Success Add Team Member");

    }

}
