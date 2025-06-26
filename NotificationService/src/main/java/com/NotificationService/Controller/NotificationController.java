package com.NotificationService.Controller;

import com.NotificationService.Entity.ChatMessage;
import com.NotificationService.Service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<ChatMessage> getAllMessages(){
        List<ChatMessage> list = notificationService.getAllMessages();

        return list;
    }

    @GetMapping("/{roomId}/top-liked")
    public List<ChatMessage> topLikedMessagesOfTheRoomForPast50Messages(@PathVariable String roomId){
        try {
            return notificationService.top5LikedMessagesOfPast50Messages(roomId);
        }
        catch (Exception e){
            log.error("Top-Liked Messages : " + e.getMessage());
            return null;
        }
    }
}
