package com.learning.backend.payment.services.gateways;

import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayStrategy {

    private final StripeProcessor stripeProcessor;

    public PaymentGatewayStrategy(StripeProcessor stripeProcessor) {
        this.stripeProcessor = stripeProcessor;
    }

    public PaymentProcessor getPaymentProcessor() {
        // In a real scenario, logic could be added here to select different gateways
        // based on criteria like currency, amount, or user preference.
        return stripeProcessor;
    }
}
