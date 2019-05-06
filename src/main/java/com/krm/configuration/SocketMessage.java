package com.krm.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krm.model.Message;
import org.springframework.web.socket.WebSocketMessage;

public class SocketMessage implements WebSocketMessage<Message> {

    private Message message;
    private String payload;

    private ObjectMapper objectMapper;

    public SocketMessage(Message message) {
        this.message = message;
        this.objectMapper = new ObjectMapper();

        try {

            this.payload = objectMapper.writeValueAsString(message);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Message getPayload() {
        return message;
    }

    @Override
    public int getPayloadLength() {
        return payload.length();
    }

    @Override
    public boolean isLast() {
        return true;
    }
}
