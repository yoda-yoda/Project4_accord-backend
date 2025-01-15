package org.noteam.be.teamMember.service;

import org.noteam.be.teamMember.dto.InviteMemberResponse;

public interface SendEmailService {

    InviteMemberResponse sendInviteEmail(String inviter, long teamId, long memberId);

}
