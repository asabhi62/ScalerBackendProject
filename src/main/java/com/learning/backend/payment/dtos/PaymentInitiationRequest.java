package com.learning.backend.payment.dtos;

import lombok.Data;

@Data
public class PaymentInitiationRequest {
    private String orderId;
    private String userId;
    private String email;
    private String phoneNumber;
    private Long amount;
}
