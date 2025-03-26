package org.dismefront.payment;

import org.dismefront.api.CreatePaymentReq;
import org.dismefront.publicatoin.PublicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    public Payment createPayment(CreatePaymentReq req) {
        Payment payment = new Payment();
        payment.setAmount(req.getAmount());
        payment.setDueDate(req.getDueDate());
        payment.setStartDate(new Date());
        payment.setSubscriptionType(req.getSubscriptionType());
        payment.setPublication(publicationRepository.findById(req.getPublicationId()).orElse(null));
        return paymentRepository.save(payment);
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id).orElseThrow(() -> new RuntimeException("Payment not found"));
    }
}