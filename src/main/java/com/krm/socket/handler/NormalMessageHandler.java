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
public class NormalMessageHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private SocketHandler socketHandler;

    public void onMessageReceive(WebSocketSession session, HashMap<String, String> messageMap) {

        User senderUser = (User) session.getAttributes().get(CURRENT_USER);

        String receiverUsername = messageMap.get(MessageConstant.RECEIVER.name());
        String messageText = messageMap.get(MessageConstant.MESSAGE_TEXT.name());
        String messageId = messageMap.get(MessageConstant.MESSAGE_ID.name());

//        User receiverUser = userService.findUserByUsername(receiverUsername);

        // Create new message
        HashMap<MessageConstant, Object> newMessage = new LinkedHashMap<>();

        newMessage.put(MessageConstant.MESSAGE_ID, messageId);
        newMessage.put(MessageConstant.SENDER_ID, senderUser.getId());
        newMessage.put(MessageConstant.SENDER, senderUser.getUsername());
        newMessage.put(MessageConstant.SENDER_NAME, senderUser.getFullName());
        newMessage.put(MessageConstant.MESSAGE_TEXT, messageText);
        newMessage.put(MessageConstant.MESSAGE_DATE, new Date());

        // Receiver is online?
        Optional<WebSocketSession> receiverStreamSession = socketHandler.getSessionByUsername(receiverUsername);

        if (receiverStreamSession.isPresent()) {
            socketHandler.sendMessage(receiverStreamSession.get(), MessageType.NORMAL, newMessage);
        }

        // Put the message into database here

        // Create sent notification
        newMessage = new LinkedHashMap<>();
        newMessage.put(MessageConstant.MESSAGE_DATE, new Date());
        newMessage.put(MessageConstant.MESSAGE_ID, messageId);

        socketHandler.sendMessage(session, MessageType.SENT_NOTIFICATION, newMessage);
    }


}
