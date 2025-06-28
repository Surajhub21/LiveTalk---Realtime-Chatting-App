package com.ModerationService.Controller;

import com.ModerationService.Entity.BannedRequest;
import com.ModerationService.Service.ModerationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/moderation")
public class ModerationController {

    private ModerationService moderationService;

    public ModerationController(ModerationService moderationService) {
        this.moderationService = moderationService;
    }

    @PostMapping("/ban")
    public ResponseEntity<?> bannedUser(@RequestBody BannedRequest bannedRequest){
        try {
           boolean isDone =  moderationService.banUser(bannedRequest);
           return ResponseEntity.status(HttpStatus.OK).body(isDone);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(false);
        }
    }

    @DeleteMapping("/unban")
    public ResponseEntity<BannedRequest> unbanUser(@RequestBody BannedRequest request) {

        try {
            moderationService.deleteByRoomIdAndUsername(request.getRoomId(), request.getUsername());
            return ResponseEntity.ok(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/banned-users/{roomId}")
    public List<String> getBannedUsers(@PathVariable String roomId) {
        try {
            return moderationService.listUser(roomId);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return null;
        }

    }


}
