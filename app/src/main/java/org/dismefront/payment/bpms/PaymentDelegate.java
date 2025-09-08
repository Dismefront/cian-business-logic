package org.dismefront.payment.bpms;

import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.dismefront.payment.PaymentService;
import org.dismefront.payment.dto.UserPayRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("paymentDelegate")
public class PaymentDelegate implements JavaDelegate {

    @Autowired
    private PaymentService paymentService;

    @Override
    public void execute(org.camunda.bpm.engine.delegate.DelegateExecution execution) throws Exception {
        String orderUUID = (String) execution.getVariable("orderUUID");
        String amount = (String) execution.getVariable("textfield_vvcvr");

        System.out.println("debug payment delegate: " + execution.getVariables());
        UserPayRequest userPayRequest = new UserPayRequest(orderUUID, Double.valueOf(amount));
        try {
            paymentService.createPayment(userPayRequest);
        }
        catch (Exception ex) {
            return;
        }
    }
}
