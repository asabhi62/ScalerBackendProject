package com.learning.backend.payment.services.gateways;

import com.learning.backend.payment.dtos.PaymentLinkResponse;

public interface PaymentProcessor {
    PaymentLinkResponse createPaymentLink(String orderId, String email, String phoneNumber, Long amount);
}
