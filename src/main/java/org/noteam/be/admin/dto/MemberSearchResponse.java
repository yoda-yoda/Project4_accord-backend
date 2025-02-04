package org.noteam.be.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noteam.be.member.domain.Role;
import org.noteam.be.member.domain.Status;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MemberSearchResponse {
    private Long memberId;
    private String nickname;
    private Status status;
    private Role role;
    private String provider;
    private LocalDateTime createdAt;
}
