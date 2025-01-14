package org.noteam.be.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.Member.Status;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthSignUpRequest {

    private String email;
    private String nickname;
    private Status status;

    public static OAuthSignUpRequest from(Member member) {
        return OAuthSignUpRequest.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .status(member.getStatus())
                .build();
    }

}