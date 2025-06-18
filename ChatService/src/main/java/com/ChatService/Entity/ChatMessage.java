package com.ChatService.Entity;

import lombok.Builder;
import lombok.Data;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chat_messages")
public class ChatMessage {

    @Id
    private String id;

    private String sender;
    private String content;
    private String roomId;
    private MessageType type;

    private int likeCount;
    private LocalDateTime createdAt;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        KICKED
    }
}

