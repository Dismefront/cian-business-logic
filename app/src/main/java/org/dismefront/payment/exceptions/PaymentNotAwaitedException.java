package org.dismefront.payment.exceptions;

public class PaymentNotAwaitedException extends RuntimeException {
    public PaymentNotAwaitedException(String message) {
        super(message);
    }
}
