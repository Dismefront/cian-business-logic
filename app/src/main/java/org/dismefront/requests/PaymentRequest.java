package org.dismefront.requests;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.dismefront.publicatoin.Publication;
import org.dismefront.publicatoin.PublicationPriority;

@Data
@Entity
@Table(name = "blps_payment_request")
public class PaymentRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="requested_priority")
    @Enumerated(EnumType.STRING)
    private PublicationPriority requestedPriority;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "publication_id", referencedColumnName = "id")
    @JsonManagedReference
    private Publication publication;

    @Column(name = "order_uuid")
    private String orderUUID;
}
