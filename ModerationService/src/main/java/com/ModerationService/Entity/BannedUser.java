package com.ModerationService.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
public class BannedUser {

    @Id
    private String id;
    private String roomId;
    private List<String> usernames =  new ArrayList<>();
}
