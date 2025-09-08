package org.dismefront.moderation.bpms;

import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.dismefront.publicatoin.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("moderatorActionsDelegate")
public class ModeratorActionsDelegate implements JavaDelegate {

    @Autowired
    private PublicationService publicationService;

    public void blockUserById(Long id) {
        System.out.println("user blocked " + id);
    }

    @Override
    public void execute(org.camunda.bpm.engine.delegate.DelegateExecution execution) throws Exception {
        String blockUserId = execution.getVariable("textfield_6fo67").toString();
        System.out.println(execution.getVariables());
        if (!blockUserId.isEmpty()) {
            blockUserById(Long.parseLong(blockUserId));
        }
        String approvePublicationId = execution.getVariable("textfield_b2abjs").toString();
        if (!approvePublicationId.isEmpty()) {
            publicationService.approvePublication(Long.parseLong(approvePublicationId));
        }
        String rejectPublicationId = execution.getVariable("textfield_zwqta").toString();
        if (!rejectPublicationId.isEmpty()) {
            publicationService.rejectPublication(Long.parseLong(rejectPublicationId));
        }
    }
}
