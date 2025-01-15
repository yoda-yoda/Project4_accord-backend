package org.noteam.be.Email.Service;

import org.noteam.be.dto.InviteMemberResponce;

public interface TeamInviteResponseService {

    InviteMemberResponce AcceptTeamInvite(Long teamId, Long memberId);
}
