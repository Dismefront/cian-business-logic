package org.dismefront.helloworld;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders-service/probes")
public class HelloWorldController {

    @GetMapping("/live")
    public ResponseEntity live() {
        return ResponseEntity.ok("live");
    }

}
