package org.noteam.be.member.service;

import org.noteam.be.member.dto.FacadeMemberProfileResponse;

public interface FacadeMemberService {
    public FacadeMemberProfileResponse getFacadeMemberProfileResponse(Long memberId);
}
