package org.dismefront.order;

import org.dismefront.order.dto.OrderPlacement;
import org.dismefront.order.exceptions.OrderNotFoundException;
import org.dismefront.payment.Payment;
import org.dismefront.payment.exceptions.InsufficientFundsException;
import org.dismefront.payment.exceptions.PaymentNotAwaitedException;
import org.dismefront.publicatoin.Publication;
import org.dismefront.publicatoin.PublicationRepository;
import org.dismefront.requests.PaymentRequest;
import org.dismefront.requests.PaymentRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.UUID;

import static java.lang.Math.abs;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRequestRepository paymentRequestRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private Double EPS = 0.0001;

    public OrderPlacement placeOrder(Double expectedAmount) {
        Order order = new Order();
        order.setOrderUUID(UUID.randomUUID().toString());
        order.setExpectedAmount(expectedAmount);
        order.setStatus(OrderStatus.PENDING);

        return new OrderPlacement(orderRepository.save(order));
    }

    public Order attachPayment(Payment payment) throws Exception {
        DefaultTransactionDefinition attachPaymentTxDef = new DefaultTransactionDefinition();
        attachPaymentTxDef.setName("attachPaymentTransaction");
        attachPaymentTxDef.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        TransactionStatus txStatus = transactionManager.getTransaction(attachPaymentTxDef);

        Order order = orderRepository.findByOrderUUID(payment.getOrderUUID());
        try {
            if (order == null) {
                throw new OrderNotFoundException("Order not found");
            }
            if (order.getStatus() == OrderStatus.REJECTED) {
                throw new PaymentNotAwaitedException("Order is already rejected");
            }
            if (order.getStatus() == OrderStatus.RESOLVED) {
                throw new PaymentNotAwaitedException("Order is already resolved");
            }
            if (abs(order.getExpectedAmount() - payment.getAmount()) > EPS) {
                throw new InsufficientFundsException("Payment amount does not match expected amount");
            }

            order.setAmountPayed(payment.getAmount());
            order.setStatus(OrderStatus.RESOLVED);
            order = orderRepository.save(order);
            approvePublicationByOrderUUID(order.getOrderUUID());

            transactionManager.commit(txStatus);

            return order;
        }
        catch (InsufficientFundsException e) {
            transactionManager.rollback(txStatus);
            saveRejectedOrder(order);
            throw e;
        }
    }

    public void saveRejectedOrder(Order order) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("saveRejectedOrderTransaction");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        TransactionStatus txStatus = transactionManager.getTransaction(def);

        try {
            order.setStatus(OrderStatus.REJECTED);
            orderRepository.save(order);

            transactionManager.commit(txStatus);
        } catch (Exception ex) {
            transactionManager.rollback(txStatus);
        }
    }

    public void approvePublicationByOrderUUID(String orderUUID) {
        PaymentRequest paymentRequest = paymentRequestRepository.findByOrderUUID(orderUUID);
        Publication publication = paymentRequest.getPublication();
        publication.setPublicationPriority(paymentRequest.getRequestedPriority());
        publicationRepository.save(publication);
    }

}
