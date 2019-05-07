package com.krm.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krm.model.User;
import com.krm.socket.handler.NormalMessageHandler;
import com.krm.socket.handler.ReceivedNotificationHandler;
import com.krm.socket.handler.WelcomeMessageHandler;
import com.krm.socket.message.MessageType;
import com.krm.socket.message.SocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.krm.contants.Constants.CURRENT_USER;

@Service
public class SocketHandler extends TextWebSocketHandler {

    private List<WebSocketSession> sessions;
    private ObjectMapper objectMapper;

    @Autowired
    private NormalMessageHandler normalMessageHandler;

    @Autowired
    private WelcomeMessageHandler welcomeMessageHandler;

    @Autowired
    private ReceivedNotificationHandler receivedNotificationHandler;

    public SocketHandler() {

        sessions = new CopyOnWriteArrayList<>();
        objectMapper = new ObjectMapper();
    }

    public TextMessage createMessage(Object object) throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(object);
        return new TextMessage(payload);
    }

    public <T> T parseMessage(TextMessage textMessage, Class<T> clazz) throws IOException {
        String payload = textMessage.getPayload();
        return objectMapper.readValue(payload, clazz);
    }

    public Optional<WebSocketSession> getSessionByUsername(String username) {
        return sessions.stream().filter(socketSession -> {
            User currentUser = (User) socketSession.getAttributes().get(CURRENT_USER);
            return currentUser.getUsername().equals(username);
        }).findFirst();
    }

    public boolean sendMessage(WebSocketSession receiver, MessageType messageType, Object object) {

        try {

            if (receiver.isOpen()) {

                SocketMessage socketMessage = new SocketMessage(messageType, object);
                receiver.sendMessage(createMessage(socketMessage));

                return true;
            }

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        return false;
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        exception.printStackTrace(System.err);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        welcomeMessageHandler.onConnected(session);

        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        SocketMessage socketMessage = parseMessage(message, SocketMessage.class);

        HashMap messageObject = (HashMap) socketMessage.getMessage();

        switch (socketMessage.getType()) {
            case NORMAL:
                normalMessageHandler.onMessageReceive(session, messageObject);
                break;
            case RECEIVED_NOTIFICATION:
                System.out.println(socketMessage.getMessage());
                receivedNotificationHandler.onMessageReceivedByUser(session, messageObject);
                break;
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }
}
