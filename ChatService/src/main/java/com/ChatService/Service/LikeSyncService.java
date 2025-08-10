package com.ChatService.Service;

import com.ChatService.Entity.ChatMessage;
import com.ChatService.Repository.ChatMessageRepository;
import com.ChatService.Repository.MongoQueryIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LikeSyncService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private MongoQueryIMPL messageRepo;
    @Autowired
    private ChatMessageRepository  messageRepository;

    private static final String LIKE_COUNT_KEY_PREFIX = "chat:likeCount:";
    private static final String LIKED_USERS_KEY_PREFIX = "chat:likedUsers:";

    @Scheduled(fixedRate = 60000) // every 1 min
    public void syncLikesToDB() {
        Set<String> keys = redisTemplate.keys(LIKE_COUNT_KEY_PREFIX + "*");
        if (keys == null || keys.isEmpty()) return;

        for (String key : keys) {
            String messageId = key.replace(LIKE_COUNT_KEY_PREFIX, "");
            Integer likeCount = (Integer) redisTemplate.opsForValue().get(key);
            Set<Object> likedUsers = redisTemplate.opsForSet()
                    .members(LIKED_USERS_KEY_PREFIX + messageId);

            if (likeCount != null) {
                ChatMessage chatMessageById = messageRepo.findChatMessageById(messageId);
                chatMessageById.setLikeCount(likeCount);
                chatMessageById.setLikedByUsers(likedUsers.stream().map(Object::toString).collect(Collectors.toSet()));

                messageRepository.save(chatMessageById);
            }
        }
    }
}
