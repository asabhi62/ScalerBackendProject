package com.learning.backend.payment.services.gateways;

import com.learning.backend.payment.dtos.PaymentLinkResponse;
import com.stripe.Stripe;
import com.stripe.model.Price;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeProcessor implements PaymentProcessor {

    @Value("${stripe.secret.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public PaymentLinkResponse createPaymentLink(String orderId, String email, String phoneNumber, Long amount) {
        // Stripe.apiKey is set in init()

        try {
            // Create Price
            Map<String, Object> priceParams = new HashMap<>();
            priceParams.put("unit_amount", amount);
            priceParams.put("currency", "inr");
            
            Map<String, Object> productData = new HashMap<>();
            productData.put("name", "Order #" + orderId);
            priceParams.put("product_data", productData);

            Price price = Price.create(priceParams);

            // Create Checkout Session
            SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/payment/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:8080/payment/cancel")
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setPrice(price.getId())
                        .setQuantity(1L)
                        .build()
                )
                .setCustomerEmail(email)
                .build();

            Session session = Session.create(params);
            
            return new PaymentLinkResponse(session.getId(), session.getUrl());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create payment link with Stripe: " + e.getMessage(), e);
        }
    }
}
