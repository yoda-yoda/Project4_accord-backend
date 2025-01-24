package org.noteam.be.image.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.noteam.be.system.util.ValidationMessage;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UploadImageRequest {

    @NotBlank(message = ValidationMessage.CANNOT_BLANK_FILE)
    private MultipartFile file;

}
