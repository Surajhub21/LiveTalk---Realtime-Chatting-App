package com.PaymentGateway.Controller;

import com.PaymentGateway.Entity.ProductRequest;
import com.PaymentGateway.Entity.StripeResponse;
import com.PaymentGateway.Entity.SuperChatRequest;
import com.PaymentGateway.Service.ChatClient;
import com.PaymentGateway.Service.StripeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/payment")
public class StripeController {

    private StripeService stripeService;
    private ChatClient client;

    public StripeController(StripeService stripeService , ChatClient client) {
        this.stripeService = stripeService;
        this.client = client;
    }

    @PostMapping
    public ResponseEntity<StripeResponse> checkoutProduct(@RequestBody ProductRequest productRequest){

        StripeResponse stripeResponse = stripeService.checkoutProducts(productRequest);
        try {

            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

            scheduler.schedule(() -> {

                if(stripeResponse.getStatus().equals("SUCCESS")){
                    SuperChatRequest superChatRequest = new SuperChatRequest();
                    superChatRequest.setAmount(productRequest.getAmount());
                    superChatRequest.setMessage(productRequest.getMessage());
                    superChatRequest.setUsername(productRequest.getName());
                    superChatRequest.setRoomId(productRequest.getRoomId());

                    client.sendPaymentSuccessMessage(superChatRequest);

                    log.info("Sned superchat Message :- " + superChatRequest);
                }

            }, 5, TimeUnit.SECONDS);

        }
        catch (Exception e){
            log.error(e.getMessage());
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(stripeResponse);
    }
}
