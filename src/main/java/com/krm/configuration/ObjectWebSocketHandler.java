package com.krm.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krm.exception.InvalidUserException;
import com.krm.model.Message;
import com.krm.model.User;
import com.krm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

import static com.krm.contants.Constants.CURRENT_USER;

@Service
@Configurable
public class ObjectWebSocketHandler extends AbstractWebSocketHandler {

    @Autowired
    protected UserService userService;

    @PostConstruct
    public void postConstruct() {
        System.out.println("Constructed!");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> socketMessage) throws Exception {

        if (socketMessage instanceof TextMessage) {

            Object payload = socketMessage.getPayload();

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> payloadMap = objectMapper.readValue(payload.toString(), Map.class);

            if (!payloadMap.containsKey("receiver")
                    || !payloadMap.containsKey("message")) {


                // Exception need to handler in socket fashion.
            }

            User receiver = userService.findUserByUsername(payloadMap.get("receiver"));

            if (receiver == null) {
                throw new InvalidUserException();
            }

            User sender = (User) session.getAttributes().get(CURRENT_USER);

            Message message = new Message();
            message.setMessageText(payloadMap.get("message"));
            message.setReceiver(receiver);
            message.setSender(sender);

            this.handleMessage(session, new SocketMessage(message));
        }

    }

    public void handleMessage(WebSocketSession session, SocketMessage message) throws IOException {

    }
}
