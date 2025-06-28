package com.ChatService.Controller.WebSocket;

import com.ChatService.Config.WebSocketEventListener;
import com.ChatService.Entity.ChatMessage;
import com.ChatService.Entity.SuperChatRequest;
import com.ChatService.Service.ChatMessageService;
import com.ChatService.Service.ModerationClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final WebSocketEventListener eventListener;
    private final SimpMessagingTemplate messagingTemplate;

    private final ChatMessageService chatService;



    // 🟢 Send & Save Message
    @MessageMapping("/chat/{roomId}/sendMessage")
    public void sendMessage(@DestinationVariable String roomId,
                            @Payload ChatMessage chatMessage) {
        chatMessage.setRoomId(roomId);
        chatMessage.setType(ChatMessage.MessageType.CHAT);
        chatMessage.setCreatedAt(LocalDateTime.now());

        // Send to clients
        messagingTemplate.convertAndSend("/topic/" + roomId, chatMessage);

        //Save in db Async
        chatService.saveMessage(chatMessage);
    }

    //  Add User
    @MessageMapping("/chat/{roomId}/addUser")
    @SendTo("/topic/{roomId}")
    public ChatMessage addUser(@DestinationVariable String roomId,
                               @Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {

        String sessionId = headerAccessor.getSessionId();
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("roomId", roomId);

        eventListener.registerUser(sessionId, chatMessage.getSender(), roomId);

        chatMessage.setRoomId(roomId);
        chatMessage.setType(ChatMessage.MessageType.JOIN);
        return chatMessage;
    }

    // 🟢 Track Like
    @MessageMapping("/chat/{roomId}/likeMessage")
    public void likeMessage(@DestinationVariable String roomId,
                            @Payload ChatMessage payload) {
        try {

            log.info("Message :- {}" , payload);

            ChatMessage updated = chatService.likeMessage(payload);

            if(updated != null) {
                // Broadcast updated like to all clients
                messagingTemplate.convertAndSend("/topic/" + roomId + ".like", updated);

            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    // 🟢 Get Users
    @MessageMapping("/chat/{roomId}/getUsers")
    public void getUsers(@DestinationVariable String roomId) {
        eventListener.broadcastUserList(roomId);
    }
}
