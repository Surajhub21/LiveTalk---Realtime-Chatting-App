package com.NotificationService.Service;

import com.NotificationService.Entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MongoCriteria {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<ChatMessage> getTop5LikedMessages(String roomId) {
        MatchOperation matchRoom = Aggregation.match(Criteria.where("roomId").is(roomId));
        UnwindOperation unwindMessages = Aggregation.unwind("chatMessageList");
        SortOperation sortByCreatedAtDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "chatMessageList.createdAt"));
        LimitOperation limitTo50 = Aggregation.limit(50);
        SortOperation sortByLikeCountDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "chatMessageList.likeCount"));
        LimitOperation limitToTop5 = Aggregation.limit(5);

        ProjectionOperation projectMessage = Aggregation.project()
                .and("chatMessageList.id").as("id")
                .and("chatMessageList.sender").as("sender")
                .and("chatMessageList.content").as("content")
                .and("chatMessageList.roomId").as("roomId")
                .and("chatMessageList.type").as("type")
                .and("chatMessageList.likeCount").as("likeCount")
                .and("chatMessageList.likedByUsers").as("likedByUsers")
                .and("chatMessageList.createdAt").as("createdAt");

        Aggregation aggregation = Aggregation.newAggregation(
                matchRoom,
                unwindMessages,
                sortByCreatedAtDesc,
                limitTo50,
                sortByLikeCountDesc,
                limitToTop5,
                projectMessage
        );

        AggregationResults<ChatMessage> results = mongoTemplate.aggregate(
                aggregation,
                "SaveMessages",  // ✅ Explicit collection name
                ChatMessage.class
        );

        return results.getMappedResults();
    }
}
