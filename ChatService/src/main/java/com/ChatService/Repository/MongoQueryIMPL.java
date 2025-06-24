package com.ChatService.Repository;

import com.ChatService.Entity.ChatMessage;
import com.ChatService.Entity.SaveMessages;
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

    public SaveMessages findRoomMessagesByRoomId(String roomId){
        Query query = new Query();
        query.addCriteria(Criteria.where("roomId").is(roomId));

        return mongoTemplate.findOne(query , SaveMessages.class);
    }
}
