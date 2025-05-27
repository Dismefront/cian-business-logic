package org.dismefront.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dismefront.futures.CompletableFutureObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentAttachEvent implements CompletableFutureObject {
    String id;
    PaymentDTO payment;
}
