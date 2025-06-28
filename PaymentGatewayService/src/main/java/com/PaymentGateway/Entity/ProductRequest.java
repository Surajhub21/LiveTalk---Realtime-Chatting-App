package com.PaymentGateway.Entity;

import lombok.Data;

@Data
public class ProductRequest {

    private Long amount;
    private String name;
    private String roomId;
    private String message;
    private String currency;

}
