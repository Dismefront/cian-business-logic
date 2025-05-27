package org.dismefront.payment;

import org.dismefront.conf.kafka.PaymentAttachFuturesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentAttachmentResponseListener {

    @Autowired
    private PaymentAttachFuturesConfig orderPlacementFuturesConfig;

    @KafkaListener(topics = "payment-attach-complete", groupId = "dismefront-group")
    public void onStatus(PaymentAttachEvent event) {
        orderPlacementFuturesConfig.completeFuture(event);
    }

}
