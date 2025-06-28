package com.ChatService.Controller;

import com.ChatService.Config.WebSocketEventListener;
import com.ChatService.Entity.BannedRequest;
import com.ChatService.Entity.ChatMessage;
import com.ChatService.Entity.SuperChatRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatRestController {

    private final WebSocketEventListener webSocketEventListener;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatRestController(WebSocketEventListener webSocketEventListener , SimpMessagingTemplate simpMessagingTemplate) {
        this.webSocketEventListener = webSocketEventListener;
        this.messagingTemplate = simpMessagingTemplate;
    }

    @GetMapping("/c")
    public String chat(){
        return "ChatService is Running";
    }

    @PostMapping("/payment")
    public void sendSuperChatMessage(@RequestBody SuperChatRequest request){

        log.info("Get Super Chat :- " + request);

        messagingTemplate.convertAndSend("/topic/" + request.getRoomId() + ".superchat",request);
    }

    @PostMapping("/kick")
    public ResponseEntity<?> kickAUser(@RequestBody BannedRequest bannedRequest){
        try {

            boolean isDone = webSocketEventListener.kickUserFromRoom(bannedRequest.getUsername(), bannedRequest.getRoomId());
            return new ResponseEntity<>(isDone , HttpStatus.OK);

        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
        }
    }
}
