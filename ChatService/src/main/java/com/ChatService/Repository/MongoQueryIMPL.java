package com.ChatService.Repository;

import com.ChatService.Entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MongoQueryIMPL {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<ChatMessage> findTop50ByRoomIdOrderByCreatedAtDesc(String roomId){
        Query query = new Query();
        query.addCriteria(Criteria.where("roomId").is(roomId));
        query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
        query.limit(50);

        return mongoTemplate.find(query, ChatMessage.class);
    }
}
