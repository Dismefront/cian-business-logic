package org.dismefront.requests.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.dismefront.publicatoin.PublicationPriority;

@Data
@NoArgsConstructor
public class PaymentRequestDTO {
    private Long id;

    private PublicationPriority requestedPriority;

    private Long publicationId;

    private String orderUUID;
}
