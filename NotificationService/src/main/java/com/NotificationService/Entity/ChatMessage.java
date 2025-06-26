package com.NotificationService.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Document("Chat-Messages")
public class ChatMessage {

    private String id;

    private String sender;
    private String content;
    private String roomId;
    private MessageType type;

    private int likeCount;
    private Set<String> likedByUsers = new HashSet<>();;
    private LocalDateTime createdAt;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        KICKED
    }
}

