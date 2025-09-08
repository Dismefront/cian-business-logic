package org.dismefront.moderation.bpms;

import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("adminActionsDelegate")
public class AdminActionsDelegate implements JavaDelegate {
    @Override
    public void execute(org.camunda.bpm.engine.delegate.DelegateExecution execution) throws Exception {
        System.out.println("Granted a role successfully, execution variables: " + execution.getVariables());
    }
}
