package com.learning.backend.payment.repositories;

import com.learning.backend.payment.entities.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Long> {
    PaymentDetails findFirstByPaymentLinkId(String paymentLinkId);
}
