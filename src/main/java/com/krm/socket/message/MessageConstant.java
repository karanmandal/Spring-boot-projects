package com.krm.socket.message;

public enum MessageConstant {

    MESSAGE_ID,
    MESSAGE_DATE,
    MESSAGE_TEXT,
    SENDER_ID,
    SENDER,
    SENDER_NAME,
    RECEIVER,
    RECEIVER_ID,
    USERNAME;

    @Override
    public String toString() {
        return this.name();
    }
}
