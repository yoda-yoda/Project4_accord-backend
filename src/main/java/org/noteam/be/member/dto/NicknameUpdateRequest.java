package org.noteam.be.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NotBlank
@NotNull
@NoArgsConstructor
@AllArgsConstructor
public class NicknameUpdateRequest {

    private String nickname;

}