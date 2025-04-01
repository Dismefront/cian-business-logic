package org.dismefront.payment.dto;

import lombok.Data;

@Data
public class UserPayRequest {

    private String orderUUID;
    private Double amount;

    public UserPayRequest(String orderUUID, Double amount) {
        this.orderUUID = orderUUID;
        this.amount = amount;
    }

}
