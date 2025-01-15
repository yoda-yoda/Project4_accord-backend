package org.noteam.be.teamMember.service;

import org.noteam.be.teamMember.dto.InviteMemberResponce;

public interface SendEmailService {

    InviteMemberResponce sendInviteEmail(String inviter, long teamId, long memberId);

}
