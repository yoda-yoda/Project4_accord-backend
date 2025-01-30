package org.noteam.be.teamMember.service;

import org.noteam.be.teamMember.dto.InviteMemberResponse;

public interface SendEmailService {

    public InviteMemberResponse sendInviteEmail(long teamId, long memberId);

}
