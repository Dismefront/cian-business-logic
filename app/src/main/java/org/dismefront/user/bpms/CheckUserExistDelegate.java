package org.dismefront.user.bpms;

import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.dismefront.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("checkUserExistDelegate")
public class CheckUserExistDelegate implements JavaDelegate {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute(org.camunda.bpm.engine.delegate.DelegateExecution execution) throws Exception {
        String phoneNumber = (String) execution.getVariable("textfield_phoneNumber");
        try {
            userRepository.findByPhoneNumber(phoneNumber)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));
        }
        catch (UsernameNotFoundException ex) {
            execution.setVariable("userExistent", false);
            return;
        }
        execution.setVariable("userExistent", true);
    }
}
