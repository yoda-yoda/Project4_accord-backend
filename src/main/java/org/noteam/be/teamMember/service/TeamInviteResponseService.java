package org.noteam.be.teamMember.service;

import org.noteam.be.teamMember.dto.InviteMemberResponse;

public interface TeamInviteResponseService {

    InviteMemberResponse AcceptTeamInvite(Long teamId, Long memberId);

}
