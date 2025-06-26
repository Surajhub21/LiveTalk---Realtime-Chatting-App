package com.NotificationService.Service;

import com.NotificationService.Entity.ChatMessage;
import com.NotificationService.Repository.MongoRepositoryForMessages;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final MongoRepositoryForMessages repositoryForMessages;
    private final MongoCriteria mongoCriteria;

    NotificationService(MongoRepositoryForMessages mongoRepositoryForMessages , MongoCriteria mongoCriteria){
        this.repositoryForMessages = mongoRepositoryForMessages;
        this.mongoCriteria = mongoCriteria;
    }

    public List<ChatMessage> getAllMessages(){

        List<ChatMessage> list = repositoryForMessages.findAll();

        return list;

    }

    public List<ChatMessage> top5LikedMessagesOfPast50Messages(String roomId) {
        List<ChatMessage> recent50 = repositoryForMessages.findTop20ByRoomIdOrderByCreatedAtDesc(roomId);

        return recent50.stream()
                .filter(msg -> msg.getLikeCount() >= 1)
                .sorted(Comparator.comparingInt(ChatMessage::getLikeCount).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }
}
