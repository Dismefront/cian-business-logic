package org.dismefront.order.exceptions;

public class PaymentNotAwaitedException extends RuntimeException {
    public PaymentNotAwaitedException(String message) {
        super(message);
    }
}
