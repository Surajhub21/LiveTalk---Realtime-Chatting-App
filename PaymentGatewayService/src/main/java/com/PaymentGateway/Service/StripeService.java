package com.PaymentGateway.Service;

import com.PaymentGateway.Entity.ProductRequest;
import com.PaymentGateway.Entity.StripeResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    private static final Logger log = LoggerFactory.getLogger(StripeService.class);
    @Value("${stripe.secretKey}")
    private String apiKey;

    public StripeResponse checkoutProducts(ProductRequest productRequest){

        Stripe.apiKey = apiKey;

        SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(productRequest.getName()).build();

        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency(productRequest.getCurrency() == null ? "INR" : productRequest.getCurrency())
                .setUnitAmount(productRequest.getAmount() * 100)
                .setProductData(productData)
                .build();

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(priceData)
                .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/success")
                .setCancelUrl("http://localhost:8080/cancle")
                .addLineItem(lineItem)
                .build();

        Session session = null;

        try {

            session = Session.create(params);

            return StripeResponse.builder()
                    .status("SUCCESS")
                    .message("Payment session Created")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();

        }
        catch (StripeException e){
            log.error(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return StripeResponse.builder()
                .status("FAILED")
                .message("Payment Cancel")
                .build();

    }
}
