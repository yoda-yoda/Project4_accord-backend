package org.noteam.be.Email.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.noteam.be.Email.Service.TeamMemberService;
import org.noteam.be.dao.TeamMemberRepository;
import org.noteam.be.domain.Member;
import org.noteam.be.domain.Team;
import org.noteam.be.domain.TeamMember;
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
