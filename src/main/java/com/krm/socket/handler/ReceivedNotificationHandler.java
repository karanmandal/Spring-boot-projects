package com.krm.socket.handler;

import com.krm.model.User;
import com.krm.service.UserService;
import com.krm.socket.SocketHandler;
import com.krm.socket.message.MessageConstant;
import com.krm.socket.message.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;

import static com.krm.contants.Constants.CURRENT_USER;

@Service
public class ReceivedNotificationHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private SocketHandler socketHandler;

    public void onMessageReceivedByUser(WebSocketSession session, HashMap<String, String> messageMap) {

        User senderUser = (User) session.getAttributes().get(CURRENT_USER);
        System.out.println("Message received by " + senderUser.getFullName());

        System.out.println(messageMap);

        String receiverUsername = messageMap.get(MessageConstant.RECEIVER.name());
        String messageId = messageMap.get(MessageConstant.MESSAGE_ID.name());

        System.out.println(receiverUsername);

        // Receiver is online?
        Optional<WebSocketSession> receiverStreamSession = socketHandler.getSessionByUsername(receiverUsername);

        // Create received notification for sender
        HashMap<MessageConstant, Object> newMessage = new LinkedHashMap<>();
        newMessage.put(MessageConstant.MESSAGE_DATE, new Date());
        newMessage.put(MessageConstant.MESSAGE_ID, messageId);

        if (receiverStreamSession.isPresent()) {
            socketHandler.sendMessage(receiverStreamSession.get(), MessageType.RECEIVED_NOTIFICATION, newMessage);
        }

    }


}
