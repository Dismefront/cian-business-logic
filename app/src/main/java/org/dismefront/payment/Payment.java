package org.dismefront.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "blps_payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "order_uuid", nullable = false, unique = true)
    private String orderUUID;

}
