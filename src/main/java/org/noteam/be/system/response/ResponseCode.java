package org.noteam.be.system.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noteam.be.system.response.item.Message;
import org.noteam.be.system.response.item.Status;

import javax.net.ssl.SSLEngineResult;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    GET_USER_PROFILE_SUCCESS(Status.OK, Message.READ_USER),

    // team
    POST_TEAM_SUCCESS(Status.OK, Message.SAVE_TEAM),
    GET_TEAM_SUCCESS(Status.OK, Message.READ_TEAM),
    UPDATE_TEAM_SUCCESS(Status.OK, Message.UPDATE_TEAM),
    DELETE_TEAM_SUCCESS(Status.OK, Message.DELETE_TEAM),


    // joinBoard
    POST_JOIN_BOARD_SUCCESS(Status.OK, Message.SAVE_JOIN_BOARD),
    GET_JOIN_BOARD_SUCCESS(Status.OK, Message.READ_JOIN_BOARD),
    UPDATE_JOIN_BOARD_SUCCESS(Status.OK, Message.UPDATE_JOIN_BOARD),
    DELETE_JOIN_BOARD_SUCCESS(Status.OK, Message.DELETE_JOIN_BOARD),

    ;






    private int httpStatus;
    private String message;

}
