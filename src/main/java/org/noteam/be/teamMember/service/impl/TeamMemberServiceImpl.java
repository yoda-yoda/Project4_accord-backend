package org.noteam.be.teamMember.service.impl;

import lombok.RequiredArgsConstructor;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.dto.MemberProfileResponse;
import org.noteam.be.member.service.MemberService;
import org.noteam.be.profileimg.service.ProfileImgService;
import org.noteam.be.team.domain.Team;
import org.noteam.be.team.dto.TeamResponse;
import org.noteam.be.team.service.TeamService;
import org.noteam.be.teamMember.service.TeamMemberService;
import org.noteam.be.teamMember.repository.TeamMemberRepository;
import org.noteam.be.teamMember.domain.TeamMember;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamMemberServiceImpl implements TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;
    private final MemberService memberService;
    private final ProfileImgService profileImgService;

    public List<MemberProfileResponse> findMembersByTeamId(Long teamId) {
        List<TeamMember> teamMembers = teamMemberRepository.findByTeamIdAndDeletedFalse(teamId);
        return teamMembers.stream()
                .map(teamMember -> {
                    Member member = teamMember.getMember();
                    return new MemberProfileResponse(
                            member.getMemberId(),
                            member.getEmail(),
                            member.getNickname(),
                            profileImgService.getMembersProfileImg(member)
                    );
                })
                .collect(Collectors.toList());

    }

    public List<MemberProfileResponse> findMembersByEmail(String query, Long teamId) {
        List<MemberProfileResponse> allMembers = memberService.findMembersByEmail(query);

        List<Long> teamMemberIds = teamMemberRepository.findMemberIdsByTeamIdAndDeletedFalse(teamId);

        return allMembers.stream()
                .filter(member -> !teamMemberIds.contains(member.getMemberId()))
                .collect(Collectors.toList());
    }


    @Override
    public TeamMember save(Member member, Team team) {

        TeamMember build = TeamMember.builder()
                .member(member)
                .team(team)
                .build();

        return teamMemberRepository.save(build);
    }

    @Override
    public TeamMember getTeamMember(Member member) {
        return teamMemberRepository.findByMember(member);
    }
}
