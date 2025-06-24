package com.ChatService.Repository;

import com.ChatService.Entity.SaveMessages;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<SaveMessages, String> {
}
