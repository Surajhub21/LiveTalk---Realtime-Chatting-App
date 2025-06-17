package com.RoomService.Entity;

import lombok.Data;

@Data
public class JoinRoomRequest {
    private String roomId;
    private String username;
}