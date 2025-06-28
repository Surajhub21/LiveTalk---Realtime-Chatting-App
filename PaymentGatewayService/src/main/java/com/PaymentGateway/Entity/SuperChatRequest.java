package com.PaymentGateway.Entity;

import lombok.Data;

@Data
public class SuperChatRequest {

    private Long amount;
    private String roomId;
    private String username;
    private String message;

}
