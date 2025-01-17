package org.noteam.be.teamMember.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class InviteMemberResponse {

    private String message;
    private boolean result;

    @Builder
    public InviteMemberResponse(String message, boolean result) {
        this.message = message;
        this.result = result;
    }

}
