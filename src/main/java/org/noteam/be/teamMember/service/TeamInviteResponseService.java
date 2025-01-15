package org.noteam.be.teamMember.service;

import org.noteam.be.teamMember.dto.InviteMemberResponce;

public interface TeamInviteResponseService {

    InviteMemberResponce AcceptTeamInvite(Long teamId, Long memberId);
}
