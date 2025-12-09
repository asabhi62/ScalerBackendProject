package com.learning.backend.payment.controllers;

import com.learning.backend.payment.services.PaymentOrchestratorService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments/callback")
public class PaymentWebhookController {

    private final PaymentOrchestratorService paymentOrchestratorService;

    public PaymentWebhookController(PaymentOrchestratorService paymentOrchestratorService) {
        this.paymentOrchestratorService = paymentOrchestratorService;
    }

    @PostMapping
    public void stripeWebhook(@RequestBody Event event) throws StripeException {
        paymentOrchestratorService.stripeCallback(event);
    }

    @GetMapping
    public void stripeCallback(@RequestParam String session) throws StripeException {
        paymentOrchestratorService.stripeCallback(session);
    }
}
