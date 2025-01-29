package org.noteam.be.member.service;

import org.noteam.be.member.domain.Member;
import org.noteam.be.member.dto.MemberProfileResponse;
import org.noteam.be.member.dto.NicknameUpdateRequest;
import org.noteam.be.member.dto.OAuthSignUpRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

public interface MemberService {

    public List<MemberProfileResponse> findMembersByEmail(String query);

    Member registerOAuthMember(OAuthSignUpRequest request, String provider);

    String extractEmail(OAuth2User oAuth2User, String registrationId);

    String extractNickname(OAuth2User oAuth2User, String registrationId);

    String generateUniqueNickname(String baseNickname);

    MemberProfileResponse updateNickname(Long memberId, NicknameUpdateRequest request);

    void deleteMember(Long memberId);;


    Member getByMemberId(Long memberId);

    public MemberProfileResponse getMemberProfile(Long memberId);


}
