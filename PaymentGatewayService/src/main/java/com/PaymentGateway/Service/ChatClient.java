package com.PaymentGateway.Service;

import com.PaymentGateway.Entity.SuperChatRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:8083/chat" , name = "Chat-Service")
public interface ChatClient {

    @PostMapping("/payment")
    void sendPaymentSuccessMessage(@RequestBody SuperChatRequest superChatRequest);
}
