package org.dismefront.requests;

import jakarta.persistence.EntityManager;
import org.dismefront.conf.kafka.OrderPlacementFuturesConfig;
import org.dismefront.order.OrderPlacement;
import org.dismefront.publicatoin.Publication;
import org.dismefront.publicatoin.PublicationRepository;
import org.dismefront.requests.dto.PaymentRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class PaymentRequestService {

    @Autowired
    private PaymentRequestRepository paymentRequestRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private OrderPlacementFuturesConfig futuresConfig;

    @Autowired
    private PublicationRepository publicationRepository;

    public OrderPlacement createPaymentRequest(PaymentRequestDTO dto) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setRequestedPriority(dto.getRequestedPriority());
        String futureUUID = UUID.randomUUID().toString();
        OrderPlacement placement = new OrderPlacement();
        placement.setId(futureUUID);

        switch (dto.getRequestedPriority()) {
            case VIP -> {
                placement.setExpectedAmount(1000.0);
                kafkaTemplate.send("order-requests", placement);

                CompletableFuture<OrderPlacement> future = futuresConfig.createFuture(futureUUID);

                try {
                    placement = future.get(1000, java.util.concurrent.TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to get order placement for VIP request", e);
                }
            }
            case PREMIUM -> {
                placement.setExpectedAmount(2000.0);
                kafkaTemplate.send("order-requests", placement);

                CompletableFuture<OrderPlacement> future = futuresConfig.createFuture(futureUUID);

                try {
                    placement = future.get(2000, java.util.concurrent.TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to get order placement for PREMIUM request", e);
                }
            }
            default -> throw new IllegalArgumentException("Invalid request");
        };
        paymentRequest.setOrderUUID(placement.getOrderUUID());
        paymentRequest.setPublication(entityManager.getReference(Publication.class, dto.getPublicationId()));
        paymentRequestRepository.save(paymentRequest);
        return placement;
    }

    public void approvePublicationByOrderUUID(String orderUUID) {
        PaymentRequest paymentRequest = paymentRequestRepository.findByOrderUUID(orderUUID);
        Publication publication = paymentRequest.getPublication();
        publication.setPublicationPriority(paymentRequest.getRequestedPriority());
        publicationRepository.save(publication);
    }

}
