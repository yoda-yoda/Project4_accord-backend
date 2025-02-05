package org.noteam.be.chat.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Valid
@Builder
public class MessageChunkRequest {

    @NotBlank
    private Long teamId;

    @NotBlank
    private int chunkNumber;

}
