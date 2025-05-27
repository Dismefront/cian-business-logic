package org.dismefront.helloworld;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders-service/probes")
public class HelloWorldController {

    @Autowired
    KafkaProducerTest kafkaProducerTest;

    @GetMapping("/live")
    public ResponseEntity live() {
        return ResponseEntity.ok("live");
    }

    @GetMapping("/producer-test")
    public ResponseEntity testProducer() {
        kafkaProducerTest.sendMessage("dismefront-topic", "hello-world");
        return ResponseEntity.ok("producer test");
    }

}
