package com.RoomService.Repository;

import com.RoomService.Entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room , String> {

    Room getRoomByRoomId(String roomId);

}
