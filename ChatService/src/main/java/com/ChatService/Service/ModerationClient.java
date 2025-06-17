package com.ChatService.Service;

import com.ChatService.Entity.BannedRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "moderation-service", url = "http://localhost:8084/moderation")
public interface ModerationClient {

    @PostMapping("/ban")
    ResponseEntity<?> bannedUser(@RequestBody BannedRequest bannedRequest);

    @GetMapping("/banned-users/{roomId}")
    List<String> getBannedUsers(@PathVariable String roomId);
}
