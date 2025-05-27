package org.dismefront.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dismefront.futures.CompletableFutureObject;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPlacement implements CompletableFutureObject {

    private String id;

    private String orderUUID;

    private OrderStatus status;

    private Double expectedAmount;

    public OrderPlacement(String orderUUID, OrderStatus status, Double expectedAmount) {
        this.orderUUID = orderUUID;
        this.status = status;
        this.expectedAmount = expectedAmount;
    }
}
