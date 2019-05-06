package com.krm.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krm.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.krm.contants.Constants.CURRENT_USER;

@Service
public class SocketHandler extends ObjectWebSocketHandler {

    private List<WebSocketSession> sessions;
    private ObjectMapper objectMapper;

    public SocketHandler() {

        sessions = new CopyOnWriteArrayList<>();
        objectMapper = new ObjectMapper();
    }


    @Override
    public void handleMessage(WebSocketSession session, SocketMessage message) throws IOException {

        System.out.println(message.getPayload());

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        // Send basic information
        User currentUser = (User) session.getAttributes().get(CURRENT_USER);

        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(currentUser)));

        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);

    }
}
