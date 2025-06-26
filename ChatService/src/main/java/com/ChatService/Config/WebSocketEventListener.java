package com.ChatService.Config;

import com.ChatService.Entity.BannedRequest;
import com.ChatService.Entity.ChatMessage;
import com.ChatService.Service.ModerationClient;
import com.ChatService.Service.RoomClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private WebSocketSessionManager sessionManager;

    private final RoomClient roomClient;
    private final ModerationClient moderationClient;

    private final Map<String, Set<String>> roomUserMap = new ConcurrentHashMap<>();
    private final Map<String, String> sessionUserMap = new ConcurrentHashMap<>();
    private final Map<String, String> sessionRoomMap = new ConcurrentHashMap<>();

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        String username = sessionUserMap.remove(sessionId);
        String roomId = sessionRoomMap.remove(sessionId);

        if (username != null && roomId != null) {
            log.info("User Disconnected: {} from Room: {}", username, roomId);

            Set<String> users = roomUserMap.get(roomId);
            if (users != null) {
                users.remove(username);
                if (users.isEmpty()) {
                    roomUserMap.remove(roomId);
                    log.info("Room {} is now empty. Closing...", roomId);
                    closeRoom(roomId);
                }
            }

            // Broadcast updated user list
            broadcastUserList(roomId);

            // Send leave message
            ChatMessage chatMessage = ChatMessage.builder()
                    .type(ChatMessage.MessageType.LEAVE)
                    .sender(username)
                    .roomId(roomId)
                    .build();

            messagingTemplate.convertAndSend("/topic/" + roomId, chatMessage);
        }
    }

    @Async
    private void closeRoom(String roomId) {
        try {
            // Notify external service (e.g., DB or Room Microservice)
            roomClient.makeAnRoomAsClosed(roomId);

            // Remove user list (already done in caller, but just in case)
            roomUserMap.remove(roomId);

            // Remove all session mappings related to the room
            Set<String> sessionsToRemove = new HashSet<>();
            for (Map.Entry<String, String> entry : sessionRoomMap.entrySet()) {
                String sessionId = entry.getKey();
                String mappedRoomId = entry.getValue();

                if (roomId.equals(mappedRoomId)) {
                    sessionUserMap.remove(sessionId); // remove user
                    sessionsToRemove.add(sessionId);  // remove room mapping later
                }
            }

            for (String sessionId : sessionsToRemove) {
                sessionRoomMap.remove(sessionId);
            }

            log.info("Destroyed room '{}': Cleaned all related session mappings and resources", roomId);

        } catch (Exception e) {
            log.error("Failed to close and cleanup room: {}", roomId, e);
        }
    }


    public void registerUser(String sessionId, String username, String roomId) {
        sessionUserMap.put(sessionId, username);
        sessionRoomMap.put(sessionId, roomId);

        Set<String> users = roomUserMap.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet());
        users.add(username);

        System.out.println("User registered: " + username + " in room " + roomId);
        System.out.println("Current users in room: " + roomUserMap.get(roomId));
        broadcastUserList(roomId);
    }

    public void broadcastUserList(String roomId) {
        Set<String> users = roomUserMap.getOrDefault(roomId, Collections.emptySet());

        Map<String, Object> payload = new HashMap<>();
        payload.put("count", users.size());
        payload.put("users", new ArrayList<>(users));

        messagingTemplate.convertAndSend("/topic/" + roomId + ".users", payload);
    }

    public boolean kickUserFromRoom(String username, String roomId) {
        Optional<String> sessionIdOpt = sessionUserMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(username))
                .map(Map.Entry::getKey)
                .filter(sessionId -> roomId.equals(sessionRoomMap.get(sessionId)))
                .findFirst();

        if (sessionIdOpt.isPresent()) {
            String sessionId = sessionIdOpt.get();
            WebSocketSession session = sessionManager.getSession(sessionId);

            if (session != null && session.isOpen()) {
                try {
                    session.close(CloseStatus.NORMAL); // ACTUAL WebSocket disconnection
                    log.info("Kicked and disconnected user '{}' from room '{}'", username, roomId);
                } catch (IOException e) {
                    log.error("Error disconnecting user '{}'", username, e);
                }
            }

            StoreBanUser(username , roomId);
            return true;
        }

        log.warn("User '{}' not found in room '{}'", username, roomId);
        return false;
    }


    @Async
    private void StoreBanUser(String username , String roomId){
        BannedRequest ban = new BannedRequest(username , roomId);
        moderationClient.bannedUser(ban);
    }


}
