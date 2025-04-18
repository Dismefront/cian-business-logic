package org.dismefront.payment;

import org.dismefront.order.exceptions.OrderNotFoundException;
import org.dismefront.payment.dto.UserPayRequest;
import org.dismefront.payment.exceptions.InsufficientFundsException;
import org.dismefront.payment.exceptions.UnprocessablePaymentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity createPayment(@RequestBody UserPayRequest req) {
        try {
            paymentService.createPayment(req);
        }
        catch (InsufficientFundsException e) {
            return ResponseEntity.status(402).body(e.getMessage());
        }
        catch (OrderNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch (UnprocessablePaymentException e) {
            return ResponseEntity.status(422).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }

        return ResponseEntity.status(200).body("Payment accepted successfully");
    }
}
