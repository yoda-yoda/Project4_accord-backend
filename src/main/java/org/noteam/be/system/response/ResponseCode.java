package org.noteam.be.system.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noteam.be.system.response.item.Message;
import org.noteam.be.system.response.item.Status;

import javax.net.ssl.SSLEngineResult;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    GET_USER_PROFILE_SUCCESS(Status.OK, Message.READ_USER);

    private int httpStatus;
    private String message;

}
