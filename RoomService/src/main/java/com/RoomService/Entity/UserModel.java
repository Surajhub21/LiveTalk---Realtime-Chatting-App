package com.RoomService.Entity;

import lombok.Data;

import java.util.List;

@Data
public class UserModel {

    private String username;
    private UserRole role;
    public enum UserRole{
        ADMIN,
        CREATOR,
        VIEWER,
    }
}
