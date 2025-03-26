package org.dismefront.api;

import lombok.Data;

import java.util.Date;

@Data
public class CreatePaymentReq {
    private Double amount;

    private String subscriptionType;

    private Date dueDate;

    private Date startDate;

    private Long publicationId;
}
