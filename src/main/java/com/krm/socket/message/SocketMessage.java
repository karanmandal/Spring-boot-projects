package com.krm.socket.message;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class SocketMessage {

    private Date messageDate;
    private MessageType type;
    private Object message;

    public SocketMessage(MessageType type, Object message) {
        this.messageDate = new Date();
        this.type = type;
        this.message = message;
    }
}
