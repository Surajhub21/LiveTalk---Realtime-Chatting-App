package com.ChatService.Service;

import com.ChatService.Entity.ChatMessage;
import com.ChatService.Repository.ChatMessageRepository;
import com.ChatService.Repository.MongoQueryIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChatMessageService {

    private final ChatMessageRepository messageRepo;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String LIKE_COUNT_KEY_PREFIX = "chat:likeCount:";
    private static final String LIKED_USERS_KEY_PREFIX = "chat:likedUsers:";


    public ChatMessageService(ChatMessageRepository messageRepo) {
        this.messageRepo = messageRepo;
    }

    @Async
    public void saveMessage(ChatMessage message) {

        messageRepo.save(message);

    }

    public ChatMessage likeMessage(ChatMessage message) {
        String messageId = message.getId();
        String sender = message.getSender();

        String likeCountKey = LIKE_COUNT_KEY_PREFIX + messageId;
        String likedUsersKey = LIKED_USERS_KEY_PREFIX + messageId;

        Boolean alreadyLiked = redisTemplate.opsForSet().isMember(likedUsersKey, sender);

        long updatedCount;
        if (Boolean.TRUE.equals(alreadyLiked)) {
            // Unlike: remove from set, decrement count
            redisTemplate.opsForSet().remove(likedUsersKey, sender);
            updatedCount = redisTemplate.opsForValue().decrement(likeCountKey);
        } else {
            // Like: add to set, increment count
            redisTemplate.opsForSet().add(likedUsersKey, sender);
            updatedCount = redisTemplate.opsForValue().increment(likeCountKey);
        }
        // Build updated ChatMessage object from Redis data
        ChatMessage updated = new ChatMessage();
        updated.setId(messageId);
        updated.setLikeCount((int) updatedCount);

        Set<Object> likedUsers = redisTemplate.opsForSet().members(likedUsersKey);
        updated.setLikedByUsers(
                likedUsers.stream().map(Object::toString).collect(Collectors.toSet())
        );

        return updated;
    }

}