package org.noteam.be.teamMember.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InviteMemberResponce {

    private String message;
    private boolean result;

    @Builder
    public InviteMemberResponce(String message, boolean result) {
        this.message = message;
        this.result = result;
    }

}
