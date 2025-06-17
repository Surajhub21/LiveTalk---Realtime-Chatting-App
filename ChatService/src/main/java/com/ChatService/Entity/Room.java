package com.ChatService.Entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class Room {

    private String roomId;
    private String roomName;

    private String roomDescription;
    private String creatorName;
    private RoomStatus status;
    private LocalDateTime createdAt;
    private List<String> participants;
    private List<String> bannedUsers;

    public enum RoomStatus {
        ACTIVE, CLOSED
    }
}
