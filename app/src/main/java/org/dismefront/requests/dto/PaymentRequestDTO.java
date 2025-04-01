package org.dismefront.requests.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.dismefront.publicatoin.PublicationPriority;
import org.dismefront.requests.PaymentRequest;

@Data
@NoArgsConstructor
public class PaymentRequestDTO {
    private Long id;

    private PublicationPriority requestedPriority;

    private Long publicationId;

    private String orderUUID;

    public PaymentRequestDTO(PaymentRequest paymentRequest) {
        this.id = paymentRequest.getId();
        this.requestedPriority = paymentRequest.getRequestedPriority();
        this.publicationId = paymentRequest.getPublication().getId();
        this.orderUUID = paymentRequest.getOrderUUID();
    }
}
