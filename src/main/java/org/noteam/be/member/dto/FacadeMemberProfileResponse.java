package org.noteam.be.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.noteam.be.team.dto.TeamResponse;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacadeMemberProfileResponse {

    private Long memberId;
    private String email;
    private String nickname;
    private String profileImage;
    private String role;
    private List<Long> teams;

    public static FacadeMemberProfileResponse fromDto(MemberProfileResponse memberProfileResponse, List<TeamResponse> teams) {
        return FacadeMemberProfileResponse.builder()
                .memberId(memberProfileResponse.getMemberId())
                .email(memberProfileResponse.getEmail())
                .nickname(memberProfileResponse.getNickname())
                .profileImage(memberProfileResponse.getProfileImage())
                .role(memberProfileResponse.getRole())
                .teams(teams.stream().map(TeamResponse::getId).collect(Collectors.toList()))
                .build();
    }


}
