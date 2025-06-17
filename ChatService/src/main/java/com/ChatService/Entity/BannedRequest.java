package com.ChatService.Entity;

import lombok.Data;

@Data
public class BannedRequest {

    private String roomId;
    private String username;

    public BannedRequest(String username, String roomId) {
        this.username = username;
        this.roomId = roomId;
    }
}
