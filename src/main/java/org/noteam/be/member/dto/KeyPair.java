package org.noteam.be.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// Access Token, Refresh Token 을 동시에 반환
public class KeyPair {

    private String accessToken;
    private String refreshToken;

}