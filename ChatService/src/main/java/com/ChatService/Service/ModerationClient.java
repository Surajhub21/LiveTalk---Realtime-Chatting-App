package com.ChatService.Service;

import com.ChatService.Entity.BannedRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "MODERATIONSERVICE", url = "${moderation-service.url}")
public interface ModerationClient {

    @PostMapping("/moderation/ban")
    ResponseEntity<?> bannedUser(@RequestBody BannedRequest bannedRequest);

    @GetMapping("/moderation/banned-users/{roomId}")
    List<String> getBannedUsers(@PathVariable String roomId);
}
