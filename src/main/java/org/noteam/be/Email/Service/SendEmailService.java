package org.noteam.be.Email.Service;

import org.noteam.be.dto.InviteMemberResponce;

public interface SendEmailService {

    InviteMemberResponce sendInviteEmail(String inviter, long teamId, long memberId);

}
