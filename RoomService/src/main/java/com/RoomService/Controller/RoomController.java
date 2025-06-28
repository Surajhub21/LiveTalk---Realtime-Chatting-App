package com.RoomService.Controller;

import com.RoomService.Entity.CreateRoomRequest;
import com.RoomService.Entity.ErrorResponse;
import com.RoomService.Entity.JoinRoomRequest;
import com.RoomService.Entity.Room;
import com.RoomService.Exceptions.UnauthorizedRoleException;
import com.RoomService.Exceptions.UserNotFoundException;
import com.RoomService.Service.RoomServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/rooms")
@Slf4j
public class RoomController {

    private final RoomServices roomServices;

    public RoomController(RoomServices roomServices) {
        this.roomServices = roomServices;
    }

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody CreateRoomRequest request) {
        try {
            log.info(request.toString());
            Room room = roomServices.createAnRoom(request);
            CompletableFuture<Boolean> future = roomServices.setTheRoomIdToUser(request.getCreatorName(), room.getRoomId());
            log.info("Service is become:- {}" ,future.get());
            return new ResponseEntity<>(room , HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            log.error("User not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, e.getMessage()));
        } catch (UnauthorizedRoleException e) {
            log.error("Unauthorized role: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(403, e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(500, "Internal Server Error"));
        }
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Room> getRoomByRoomId(@PathVariable String roomId){
        Room room = roomServices.getRoomByRoomId(roomId);

        if(room != null){
            return new ResponseEntity<>(room , HttpStatus.OK);
        }

        return new ResponseEntity<>(null , HttpStatus.FORBIDDEN);
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinToARoom(@RequestBody JoinRoomRequest request){
        try {
            Room room = roomServices.joinToARoom(request);
            return new ResponseEntity<>(room , HttpStatus.ACCEPTED);
        } catch (UserNotFoundException e) {
            log.error("User not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, e.getMessage()));
        } catch (UnauthorizedRoleException e) {
            log.error("Unauthorized role: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(403, e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(500, "Internal Server Error"));
        }
    }

    @PutMapping("/{roomId}/close")
    public boolean makeARoomAsAClose(@PathVariable String roomId){
        try {
            return roomServices.makeARoomAsAClose(roomId);
        }
        catch (Exception e){
            log.error("Failed to update Room Status {}" , e);
            return false;
        }
    }
}
