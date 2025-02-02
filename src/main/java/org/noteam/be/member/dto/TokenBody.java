package org.noteam.be.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// JWT Tokne 에서 얻은 Claims 가 담겨짐.
public class TokenBody {

    private Long memberId;
    private String role;
    //토큰 구분을 위해 추가했음.
    private boolean isRefresh;

}