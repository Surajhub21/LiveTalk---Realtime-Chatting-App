package com.RoomService.Service;

import com.RoomService.Entity.UserModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserClient {

    @GetMapping("/users/{username}")
    UserModel getUserByUserName(@PathVariable String username);

    @PostMapping("/users/{username}/roomId/{roomId}")
    boolean setRoomIdToUser(@PathVariable String username , @PathVariable String roomId);
}
