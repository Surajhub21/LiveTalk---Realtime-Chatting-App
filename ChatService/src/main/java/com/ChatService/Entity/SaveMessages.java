package com.ChatService.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "Chat-Messages")
public class SaveMessages {

    @Id
    private String id;

    @Indexed(unique = true)
    private String roomId;

    private List<ChatMessage> chatMessageList = new ArrayList<>();
}
