package com.ChatService.Service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "ROOMSERVICE" , url = "${room-service.url}")
public interface RoomClient {

    @PutMapping("/rooms/{roomId}/close")
    boolean makeAnRoomAsClosed(@PathVariable String roomId);

}
