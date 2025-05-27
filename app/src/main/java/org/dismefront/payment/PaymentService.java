package org.dismefront.payment;

import org.dismefront.conf.kafka.PaymentAttachFuturesConfig;
import org.dismefront.payment.dto.UserPayRequest;
import org.dismefront.payment.exceptions.UnprocessablePaymentException;
import org.dismefront.requests.PaymentRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private PaymentAttachFuturesConfig futuresConfig;

    @Autowired
    private PaymentRequestService paymentRequestService;

    public void createPayment(UserPayRequest req) throws Exception {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("createPaymentTransaction");
        def.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRED);

        TransactionStatus txStatus = transactionManager.getTransaction(def);

        try {
            Payment payment = new Payment();
            payment.setOrderUUID(req.getOrderUUID());
            payment.setAmount(req.getAmount());

            if (paymentRepository.findByOrderUUID(req.getOrderUUID()) != null) {
                throw new UnprocessablePaymentException("Payment has already been processed for this order");
            }
            paymentRepository.save(payment);

            PaymentAttachEvent paymentAttachEvent = new PaymentAttachEvent();
            String futureId = UUID.randomUUID().toString();
            paymentAttachEvent.setId(futureId);
            paymentAttachEvent.setPayment(new PaymentDTO(payment.getAmount(), payment.getOrderUUID()));

            kafkaTemplate.send("payment-attach", paymentAttachEvent);

            CompletableFuture<PaymentAttachEvent> future = futuresConfig.createFuture(futureId);

            future.get(1000, java.util.concurrent.TimeUnit.MILLISECONDS);

            paymentRequestService.approvePublicationByOrderUUID(payment.getOrderUUID());

            transactionManager.commit(txStatus);
        }
        catch (Exception e) {
            transactionManager.rollback(txStatus);
            throw e;
        }
    }
}