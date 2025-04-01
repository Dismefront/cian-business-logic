package org.dismefront.requests;

import jakarta.persistence.EntityManager;
import org.dismefront.order.Order;
import org.dismefront.order.OrderService;
import org.dismefront.order.dto.OrderPlacement;
import org.dismefront.publicatoin.Publication;
import org.dismefront.publicatoin.PublicationPriority;
import org.dismefront.requests.dto.PaymentRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentRequestService {

    @Autowired
    private PaymentRequestRepository paymentRequestRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private EntityManager entityManager;

    public OrderPlacement createPaymentRequest(PaymentRequestDTO dto) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setRequestedPriority(dto.getRequestedPriority());
        OrderPlacement orderPlacement;
        switch (dto.getRequestedPriority()) {
            case VIP -> orderPlacement = orderService.placeOrder(1000.0);
            case PREMIUM -> orderPlacement = orderService.placeOrder(2000.0);
            default -> throw new IllegalArgumentException("Invalid request");
        };
        paymentRequest.setOrderUUID(orderPlacement.getOrderUUID());
        paymentRequest.setPublication(entityManager.getReference(Publication.class, dto.getPublicationId()));
        paymentRequestRepository.save(paymentRequest);
        return orderPlacement;
    }

}
