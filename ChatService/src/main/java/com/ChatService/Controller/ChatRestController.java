package com.ChatService.Controller;

import com.ChatService.Config.WebSocketEventListener;
import com.ChatService.Entity.BannedRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/chat")
public class ChatRestController {

    private final WebSocketEventListener webSocketEventListener;

    public ChatRestController(WebSocketEventListener webSocketEventListener) {
        this.webSocketEventListener = webSocketEventListener;
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
