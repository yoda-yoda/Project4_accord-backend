package org.noteam.be.teamMember.service.impl;

import lombok.RequiredArgsConstructor;
import org.noteam.be.teamMember.service.TeamMemberService;
import org.noteam.be.teamMember.repository.TeamMemberRepository;
import org.noteam.be.teamMember.domain.Member;
import org.noteam.be.teamMember.domain.Team;
import org.noteam.be.teamMember.domain.TeamMember;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamMemberServiceImpl implements TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;

    @Override
    public TeamMember save(Member member, Team team) {

        TeamMember build = TeamMember.builder()
                .member(member)
                .team(team)
                .build();

        return teamMemberRepository.save(build);
    }
}
