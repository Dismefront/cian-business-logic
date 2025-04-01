package org.dismefront.order;

import org.dismefront.order.dto.OrderPlacement;
import org.dismefront.order.exceptions.OrderNotFoundException;
import org.dismefront.payment.Payment;
import org.dismefront.payment.exceptions.InsufficientFundsException;
import org.dismefront.publicatoin.Publication;
import org.dismefront.publicatoin.PublicationRepository;
import org.dismefront.requests.PaymentRequest;
import org.dismefront.requests.PaymentRequestRepository;
import org.dismefront.requests.PaymentRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private Double EPS = 0.0001;

    public OrderPlacement placeOrder(Double expectedAmount) {
        Order order = new Order();
        order.setOrderUUID(UUID.randomUUID().toString());
        order.setExpectedAmount(expectedAmount);
        order.setStatus(OrderStatus.PENDING);

        return new OrderPlacement(orderRepository.save(order));
    }

    public Order attachPayment(Payment payment) throws Exception {
        Order order = orderRepository.findByOrderUUID(payment.getOrderUUID());
        if (order == null) {
            throw new OrderNotFoundException("Order not found");
        }
        if (order.getStatus() == OrderStatus.REJECTED) {
            throw new InsufficientFundsException("Order is already rejected");
        }
        if (order.getStatus() == OrderStatus.RESOLVED) {
            throw new InsufficientFundsException("Order is already resolved");
        }
        order.setAmountPayed(payment.getAmount());
        if (abs(order.getExpectedAmount() - payment.getAmount()) <= EPS) {
            order.setStatus(OrderStatus.RESOLVED);
            order = orderRepository.save(order);
        } else {
            order.setStatus(OrderStatus.REJECTED);
            orderRepository.save(order);
            throw new InsufficientFundsException("Payment amount does not match expected amount");
        }

        approvePublicationByOrderUUID(order.getOrderUUID());

        return order;
    }

    public void approvePublicationByOrderUUID(String orderUUID) {
        PaymentRequest paymentRequest = paymentRequestRepository.findByOrderUUID(orderUUID);
        Publication publication = paymentRequest.getPublication();
        publication.setPublicationPriority(paymentRequest.getRequestedPriority());
        publicationRepository.save(publication);
    }

}
