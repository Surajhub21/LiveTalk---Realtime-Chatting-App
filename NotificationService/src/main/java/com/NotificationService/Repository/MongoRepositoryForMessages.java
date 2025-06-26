package com.NotificationService.Repository;

import com.NotificationService.Entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MongoRepositoryForMessages extends MongoRepository<ChatMessage , String> {

    List<ChatMessage> findByRoomId(String roomId);

    List<ChatMessage> findTop20ByRoomIdOrderByCreatedAtDesc(String roomId);

}
