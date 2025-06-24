package com.ChatService.Service;

import com.ChatService.Entity.ChatMessage;
import com.ChatService.Entity.SaveMessages;
import com.ChatService.Repository.ChatMessageRepository;
import com.ChatService.Repository.MongoQueryIMPL;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
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

        SaveMessages save = mongoQueryIMPL.findRoomMessagesByRoomId(message.getRoomId());

        if(save != null){
            save.getChatMessageList().add(message);
            messageRepo.save(save);
        }else {
            save = new SaveMessages();
            save.setRoomId(message.getRoomId());
            save.getChatMessageList().add(message);
            messageRepo.save(save);
        }
    }

    public ChatMessage likeMessage(ChatMessage message) {
        SaveMessages save = mongoQueryIMPL.findRoomMessagesByRoomId(message.getRoomId());
        ChatMessage returnMessage = new ChatMessage();

        if (save != null) {
            List<ChatMessage> list = save.getChatMessageList();

            for (ChatMessage chatMessage : list) {
                if (chatMessage.getId().equals(message.getId())) {

                    if (chatMessage.getLikedByUsers() == null) {
                        chatMessage.setLikedByUsers(new HashSet<>());
                    }

                    if (!chatMessage.getLikedByUsers().contains(message.getSender())) {

                        chatMessage.getLikedByUsers().add(message.getSender());
                        chatMessage.setLikeCount(chatMessage.getLikeCount() + 1);
                    }
                    else {

                        chatMessage.getLikedByUsers().remove(message.getSender());
                        chatMessage.setLikeCount(chatMessage.getLikeCount() - 1);
                    }

                    returnMessage = chatMessage;
                    break;
                }
            }

            messageRepo.save(save);
        }

        return returnMessage;
    }

}