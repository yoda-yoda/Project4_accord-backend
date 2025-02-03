package org.noteam.be.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.noteam.be.member.domain.Status;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberStatusUpdateRequest {
    private Status status;
}
