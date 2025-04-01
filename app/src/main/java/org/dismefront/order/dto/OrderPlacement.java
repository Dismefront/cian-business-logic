package org.dismefront.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.dismefront.order.Order;
import org.dismefront.order.OrderStatus;

@Data
@AllArgsConstructor
public class OrderPlacement {

    private String orderUUID;

    private OrderStatus status;

    private Double expectedAmount;

    public OrderPlacement(Order order) {
        orderUUID = order.getOrderUUID();
        status = order.getStatus();
        expectedAmount = order.getExpectedAmount();
    }
}
