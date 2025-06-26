package com.ChatService.Service;

import com.ChatService.Entity.ChatMessage;
import com.ChatService.Repository.ChatMessageRepository;
import com.ChatService.Repository.MongoQueryIMPL;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

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

        messageRepo.save(message);

    }

    public ChatMessage likeMessage(ChatMessage message) {
        ChatMessage chatMessage = mongoQueryIMPL.findChatMessageById(message.getId());

        if (message != null) {

            if (chatMessage.getLikedByUsers() == null) {
                chatMessage.setLikedByUsers(new HashSet<>());
            }

            if (!chatMessage.getLikedByUsers().contains(message.getSender())) {

                chatMessage.getLikedByUsers().add(message.getSender());
                chatMessage.setLikeCount(chatMessage.getLikeCount() + 1);

            } else {

                chatMessage.getLikedByUsers().remove(message.getSender());
                chatMessage.setLikeCount(chatMessage.getLikeCount() - 1);
            }


            messageRepo.save(chatMessage);
        }

        return chatMessage;
    }

}