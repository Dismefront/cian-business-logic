package org.dismefront.payment.exceptions;

public class UnprocessablePaymentException extends RuntimeException {
    public UnprocessablePaymentException(String message) {
        super(message);
    }
}
