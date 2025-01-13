package org.noteam.be.member.service;

import org.noteam.be.member.domain.Member;
import org.noteam.be.member.dto.OAuthSignUpRequest;
import org.noteam.be.member.repository.MemberRepository;

import java.util.Optional;

public class MemberServiceImpl implements MemberService {

    private MemberRepository memberRepository;

    @Override
    public Member saveMember(OAuthSignUpRequest oAuthSignUpRequest) {
        Optional<Member> existingMember = memberRepository.findByEmail(OAuthSignUpRequest.getEmail());

        return memberRepository.save();
    }

}
