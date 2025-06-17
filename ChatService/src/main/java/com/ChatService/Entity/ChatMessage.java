package com.ChatService.Entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessage {

    private String sender;
    private String content;
    private String roomId;
    private MessageType type;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        KICKED
    }

}
