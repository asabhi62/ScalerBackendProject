package com.learning.backend.payment.services;

import com.learning.backend.payment.dtos.PaymentInitiationRequest;
import com.learning.backend.payment.dtos.PaymentLinkResponse;
import com.learning.backend.payment.entities.PaymentDetails;
import com.learning.backend.payment.repositories.PaymentDetailsRepository;
import com.learning.backend.payment.services.gateways.PaymentGatewayStrategy;
import com.learning.backend.payment.services.gateways.PaymentProcessor;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import org.springframework.stereotype.Service;

@Service
public class PaymentOrchestratorService {

    private final PaymentGatewayStrategy paymentGatewayStrategy;
    private final PaymentDetailsRepository paymentDetailsRepository;

    public PaymentOrchestratorService(PaymentGatewayStrategy paymentGatewayStrategy, PaymentDetailsRepository paymentDetailsRepository) {
        this.paymentGatewayStrategy = paymentGatewayStrategy;
        this.paymentDetailsRepository = paymentDetailsRepository;
    }

    public String initiatePayment(PaymentInitiationRequest request) {
        PaymentProcessor processor = paymentGatewayStrategy.getPaymentProcessor();
        PaymentLinkResponse response = processor.createPaymentLink(
                request.getOrderId(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getAmount()
        );

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setOrderId(Long.parseLong(request.getOrderId()));
        paymentDetails.setUserId(Long.parseLong(request.getUserId()));
        paymentDetails.setAmount(request.getAmount());
        paymentDetails.setStatus("INITIATED");
        paymentDetails.setPaymentLinkId(response.getId());
        
        paymentDetailsRepository.save(paymentDetails);

        return response.getUrl();
    }

    public void stripeCallback(Event event) {
        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if (session != null) {
                updatePaymentStatus(session.getId(), "SUCCESS");
            }
        }
    }

    public void stripeCallback(String sessionId) throws StripeException {
        Session session = Session.retrieve(sessionId);
        if ("paid".equals(session.getPaymentStatus())) {
            updatePaymentStatus(session.getId(), "SUCCESS");
        } else {
            updatePaymentStatus(session.getId(), "FAILED");
        }
    }

    private void updatePaymentStatus(String sessionId, String status) {
        PaymentDetails payment = paymentDetailsRepository.findFirstByPaymentLinkId(sessionId);
        if (payment != null) {
            payment.setStatus(status);
            paymentDetailsRepository.save(payment);
        }
    }
}
