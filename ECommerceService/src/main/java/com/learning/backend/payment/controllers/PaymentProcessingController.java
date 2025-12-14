package com.learning.backend.payment.controllers;

import com.learning.backend.payment.dtos.PaymentInitiationRequest;
import com.learning.backend.payment.services.PaymentOrchestratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentProcessingController {

    private final PaymentOrchestratorService paymentOrchestratorService;

    public PaymentProcessingController(PaymentOrchestratorService paymentOrchestratorService) {
        this.paymentOrchestratorService = paymentOrchestratorService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<String> initiatePayment(@RequestBody PaymentInitiationRequest request) {
        String paymentUrl = paymentOrchestratorService.initiatePayment(request);
        return ResponseEntity.ok(paymentUrl);
    }
}
