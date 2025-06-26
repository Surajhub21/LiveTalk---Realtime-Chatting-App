package com.RoomService.Service;

import com.RoomService.Entity.CreateRoomRequest;
import com.RoomService.Entity.JoinRoomRequest;
import com.RoomService.Entity.Room;
import com.RoomService.Entity.UserModel;
import com.RoomService.Exceptions.UnauthorizedRoleException;
import com.RoomService.Exceptions.UserNotFoundException;
import com.RoomService.Repository.RoomRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class RoomServices {

    private final RoomRepository roomRepository;
    private final UserClient userClient;

    public RoomServices(RoomRepository roomRepository ,UserClient userClient) {
        this.roomRepository = roomRepository;
        this.userClient = userClient;
    }

    public Room createAnRoom(CreateRoomRequest request) {
        //Check if user exists
        UserModel user = userClient.getUserByUserName(request.getCreatorName());

        if (user == null) {
            throw new UserNotFoundException("User not found with username: " + request.getCreatorName());
        }

        // 2. Check if the user is a CREATOR
        if (user.getRole() != UserModel.UserRole.CREATOR) {
            throw new UnauthorizedRoleException("User is not authorized to create a room. Only users with role 'CREATOR' can create rooms.");
        }

        Room room = new Room();
        room.setRoomName(request.getRoomName());
        room.setRoomDescription(request.getRoomDescription());
        room.setCreatorName(request.getCreatorName());
        return roomRepository.save(room);
    }

    public Room getRoomByRoomId(String roomId){
        return roomRepository.getRoomByRoomId(roomId);
    }

    public Room joinToARoom(JoinRoomRequest request){

        UserModel user = userClient.getUserByUserName(request.getUsername());

        if (user == null) {
            throw new UserNotFoundException("User not found with username: " + request.getUsername());
        }

        // 2. Check if the user is a VIEWER
        if (user.getRole() != UserModel.UserRole.VIEWER) {
            throw new UnauthorizedRoleException("User is not authorized to create a room. Only users with role 'VIEWER' can create rooms.");
        }
        Room room = getRoomByRoomId(request.getRoomId());

        if(room != null && room.getStatus() != Room.RoomStatus.CLOSED) {
            room.getParticipants().add(request.getUsername());
            roomRepository.save(room);
            return room;
        }

        throw new RuntimeException("Room is already Closed");
    }

    public boolean makeARoomAsAClose(String roomId){
        if(roomId != null) {
            Room room = roomRepository.getRoomByRoomId(roomId);
            room.setStatus(Room.RoomStatus.CLOSED);
            roomRepository.save(room);
            return true;
        }

        return false;
    }

    @Async
    public CompletableFuture<Boolean> setTheRoomIdToUser(String username , String roomId){
        boolean isDone = userClient.setRoomIdToUser(username , roomId);
        return CompletableFuture.completedFuture(isDone);
    }
}
