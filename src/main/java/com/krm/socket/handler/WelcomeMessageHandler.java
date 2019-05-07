package com.krm.socket.handler;

import com.krm.model.Message;
import com.krm.model.User;
import com.krm.socket.SocketHandler;
import com.krm.socket.message.MessageType;
import com.krm.socket.message.SocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.krm.contants.Constants.CURRENT_USER;

@Service
public class WelcomeMessageHandler {

    @Autowired
    private SocketHandler socketHandler;

    public void onConnected(WebSocketSession session) {

        try {

            User currentUser = (User) session.getAttributes().get(CURRENT_USER);

            HashMap<String, Object> newMessage = new LinkedHashMap<>();
            newMessage.put("id", currentUser.getId());
            newMessage.put("username", currentUser.getUsername());
            newMessage.put("firstName", currentUser.getFirstName());
            newMessage.put("lastName", currentUser.getLastName());
            newMessage.put("fullName", currentUser.getFullName());

            ArrayList<String> notifications = new ArrayList<>();
            notifications.add("New friends request receive");
            notifications.add("Message from UserB");
            notifications.add("Message from Karan");

            newMessage.put("notifications", notifications);

            socketHandler.sendMessage(session, MessageType.WELCOME, newMessage);
            System.out.println("Welcome message sent");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
