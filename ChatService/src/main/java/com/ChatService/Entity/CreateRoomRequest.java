package com.ChatService.Entity;

import lombok.Data;

@Data
public class CreateRoomRequest {
    private String roomName;
    private String roomDescription;
    private String creatorName;
}