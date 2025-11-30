package org.dismefront.requests;

import org.dismefront.conf.kafka.OrderPlacementFuturesConfig;
import org.dismefront.order.OrderPlacement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusCompleteListener {

    @Autowired
    private OrderPlacementFuturesConfig orderPlacementFuturesConfig;

    @KafkaListener(topics = "order-status-complete", groupId = "dismefront-group")
    public void onStatus(OrderPlacement placement) {
        orderPlacementFuturesConfig.completeFuture(placement);
    }

}
