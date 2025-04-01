package org.dismefront.payment;

import org.dismefront.order.Order;
import org.dismefront.order.OrderService;
import org.dismefront.order.OrderStatus;
import org.dismefront.payment.dto.UserPayRequest;
import org.dismefront.payment.exceptions.UnprocessablePaymentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderService orderService;

    public OrderStatus createPayment(UserPayRequest req) throws Exception {
        Payment payment = new Payment();
        payment.setOrderUUID(req.getOrderUUID());
        payment.setAmount(req.getAmount());

        if (paymentRepository.findByOrderUUID(req.getOrderUUID()) != null) {
            throw new UnprocessablePaymentException("Payment has already been processed for this order");
        }
        paymentRepository.save(payment);

        Order order = orderService.attachPayment(payment);

        return order.getStatus();
    }
}