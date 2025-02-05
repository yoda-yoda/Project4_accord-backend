package org.noteam.be.admin.service;

import lombok.RequiredArgsConstructor;
import org.noteam.be.admin.dto.MemberSearchResponse;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.Status;
import org.noteam.be.member.repository.MemberRepository;
import org.noteam.be.system.exception.member.MemberNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static org.noteam.be.system.exception.ExceptionMessage.MemberAuth.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AdminMemberService {
    private final MemberRepository memberRepository;

    // 관리자용 회원 조회 메서드
    public Page<MemberSearchResponse> searchMembers(String keyword, Pageable pageable) {
        Page<Member> members = memberRepository.searchByNickname(
                "%"+keyword+"%",
                keyword,
                pageable
        );

        return members.map(m -> new MemberSearchResponse(
                m.getMemberId(),
                m.getNickname(),
                m.getStatus(),
                m.getRole(),
                m.getProvider(),
                m.getCreatedAt()
        ));

    }

    // 계정 상태 변경 메서드 (차단이나 계정 메뉴얼삭제 등)
    public void updateMemberStatus(Long memberId, Status newStatus) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));

        member.changeStatus(newStatus);
        memberRepository.save(member);

    }
}
