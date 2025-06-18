package com.ChatService.Service;

import com.ChatService.Entity.ChatMessage;
import com.ChatService.Repository.ChatMessageRepository;
import com.ChatService.Repository.MongoQueryIMPL;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class ChatMessageService {

    private final ChatMessageRepository messageRepo;
    private final MongoQueryIMPL mongoQueryIMPL;

    public ChatMessageService(ChatMessageRepository messageRepo, MongoQueryIMPL mongoQueryIMPL) {
        this.messageRepo = messageRepo;
        this.mongoQueryIMPL = mongoQueryIMPL;
    }

    @Async
    public void saveMessage(ChatMessage message) {
        message.setId(UUID.randomUUID().toString());
        message.setCreatedAt(LocalDateTime.now());
        messageRepo.save(message);
    }

    public ChatMessage likeMessage(String messageId) {
        ChatMessage msg = messageRepo.findById(messageId).orElseThrow();
        msg.setLikeCount(msg.getLikeCount() + 1);
        return messageRepo.save(msg);
    }

    public List<ChatMessage> getTop5FromLast50(String roomId) {
        List<ChatMessage> recent = mongoQueryIMPL.findTop50ByRoomIdOrderByCreatedAtDesc(roomId);

        return recent.stream()
                .sorted(Comparator.comparing(ChatMessage::getLikeCount).reversed())
                .limit(5)
                .toList();
    }
}