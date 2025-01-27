package org.noteam.be.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfileResponse {

    private Long memberId;
    private String email;
    private String nickname;
    private String profileImage;

}
