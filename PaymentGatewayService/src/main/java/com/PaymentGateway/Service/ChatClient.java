package com.PaymentGateway.Service;

import com.PaymentGateway.Entity.SuperChatRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "${chat-service.url}" , name = "CHATSERVICE")
public interface ChatClient {

    @PostMapping("/chat/payment")
    void sendPaymentSuccessMessage(@RequestBody SuperChatRequest superChatRequest);
}
