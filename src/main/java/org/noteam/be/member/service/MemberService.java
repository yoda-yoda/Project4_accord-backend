package org.noteam.be.member.service;

import org.noteam.be.member.domain.Member;
import org.noteam.be.member.dto.OAuthSignUpRequest;

public interface MemberService {

    Member saveMember(OAuthSignUpRequest oAuthSignUpRequest);

}
