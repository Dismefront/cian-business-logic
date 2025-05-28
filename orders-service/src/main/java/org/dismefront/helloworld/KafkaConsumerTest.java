package org.dismefront.helloworld;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerTest {

    @KafkaListener(topics="dismefront-topic", groupId="dismefront-group")
    public void listen(String message) {
        System.out.println("---GOT MESSAGE---");
        System.out.println(message);
    }
}
