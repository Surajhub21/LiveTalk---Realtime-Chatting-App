package com.ChatService.Repository;

import com.ChatService.Entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class MongoQueryIMPL {

    @Autowired
    private MongoTemplate mongoTemplate;

    public ChatMessage findChatMessageById(String id){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));

        return mongoTemplate.findOne(query , ChatMessage.class);
    }
}
