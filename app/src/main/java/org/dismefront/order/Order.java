package org.dismefront.order;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "blps_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="order_uuid", nullable = false, unique = true)
    private String orderUUID;

    @Column(name = "expected_amount", nullable = false)
    private Double expectedAmount;

    @Column(name = "amount_payed")
    private Double amountPayed;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

}
