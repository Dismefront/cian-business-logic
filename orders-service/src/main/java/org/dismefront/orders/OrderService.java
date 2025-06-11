package org.dismefront.orders;

import org.dismefront.order.OrderPlacement;
import org.dismefront.order.OrderStatus;
import org.dismefront.payment.PaymentAttachEvent;
import org.dismefront.order.exceptions.InsufficientFundsException;
import org.dismefront.order.exceptions.OrderNotFoundException;
import org.dismefront.order.exceptions.PaymentNotAwaitedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private PlatformTransactionManager transactionManager;

    private Double EPS = 0.0001;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "order-requests", groupId = "dismefront-group")
    public void placeOrderListen(OrderPlacement placement) {
        Order order = new Order();
        String orderUUID = UUID.randomUUID().toString();

        placement.setOrderUUID(orderUUID);
        order.setOrderUUID(orderUUID);
        try {
            double expectedAmount = placement.getExpectedAmount();
            if (expectedAmount < 0) {
                throw new IllegalArgumentException("Expected amount cannot be negative");
            }
            order.setExpectedAmount(expectedAmount);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid expected amount format", e);
        }
        order.setStatus(OrderStatus.PENDING);
        placement.setStatus(OrderStatus.PENDING);

        orderRepository.save(order);
        kafkaTemplate.send("order-status-complete", placement);
    }

    @KafkaListener(topics = "payment-attach", groupId = "dismefront-group")
    public void attachPayment(PaymentAttachEvent event) throws Exception {
        DefaultTransactionDefinition attachPaymentTxDef = new DefaultTransactionDefinition();
        attachPaymentTxDef.setName("attachPaymentTransaction");
        attachPaymentTxDef.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        TransactionStatus txStatus = transactionManager.getTransaction(attachPaymentTxDef);

        Order order = orderRepository.findByOrderUUID(event.getPayment().getOrderUUID());
        try {
            if (order == null) {
                throw new OrderNotFoundException("Order not found");
            }
            if (order.getStatus() == OrderStatus.TIMED_OUT) {
                throw new PaymentNotAwaitedException("Order is timed out");
            }
            if (order.getStatus() == OrderStatus.REJECTED) {
                throw new PaymentNotAwaitedException("Order is already rejected");
            }
            if (order.getStatus() == OrderStatus.RESOLVED) {
                throw new PaymentNotAwaitedException("Order is already resolved");
            }
            if (Math.abs(order.getExpectedAmount() - event.getPayment().getAmount()) > EPS) {
                throw new InsufficientFundsException("Payment amount does not match expected amount");
            }

            order.setAmountPayed(event.getPayment().getAmount());
            order.setStatus(OrderStatus.RESOLVED);
            order = orderRepository.save(order);

            transactionManager.commit(txStatus);

            kafkaTemplate.send("payment-attach-complete", event);
        }
        catch (InsufficientFundsException e) {
            transactionManager.rollback(txStatus);
            saveRejectedOrder(order);
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

    @Scheduled(fixedRate = 15000)
    public void checkPendingOrders() {
        System.out.println("Checking pending orders for timeouts...");
        orderRepository.findAllByStatus(OrderStatus.PENDING).forEach(order -> {;
            if (order.getDateCreated() == null
                    || order.getDateCreated().plusMinutes(15).isBefore(LocalDateTime.now())) {
                order.setStatus(OrderStatus.TIMED_OUT);
                orderRepository.save(order);
            }
        });
    }

}
