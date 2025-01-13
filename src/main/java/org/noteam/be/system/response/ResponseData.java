package org.noteam.be.system.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Getter
@ToString
@Builder
public class ResponseData<T> {
    private final int status;
    private final String message;
    private final ResponseCode code;
    private final String timestamp;
    private T data;

    public static ResponseEntity<ResponseData> toResponseEntity(ResponseCode responseCode) {
        return ResponseEntity
                .status(responseCode.getHttpStatus())
                .body(ResponseData.builder()
                        .status(responseCode.getHttpStatus())
                        .message(responseCode.getMessage())
                        .code(responseCode)
                        .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy. M. d. a h:mm").withLocale(Locale.forLanguageTag("ko"))))
                        .build()
                );
    }

    public static <T> ResponseEntity<ResponseData<T>> toResponseEntity(ResponseCode responseCode, T data) {
        return ResponseEntity
                .status(responseCode.getHttpStatus())
                .body(ResponseData.<T>builder()
                        .status(responseCode.getHttpStatus())
                        .message(responseCode.getMessage())
                        .code(responseCode)
                        .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy. M. d. a h:mm").withLocale(Locale.forLanguageTag("ko"))))
                        .data(data)
                        .build()
                );
    }

    public static <T> ResponseEntity<ResponseData<T>> toResponseEntity(ResponseCode responseCode, MultiValueMap<String, String> header, T data) {
        return ResponseEntity
                .status(responseCode.getHttpStatus())
                .header(String.valueOf(header))
                .body(ResponseData.<T>builder()
                        .status(responseCode.getHttpStatus())
                        .message(responseCode.getMessage())
                        .code(responseCode)
                        .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy. M. d. a h:mm").withLocale(Locale.forLanguageTag("ko"))))
                        .data(data)
                        .build()
                );
    }
}
