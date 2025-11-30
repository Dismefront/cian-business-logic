package org.dismefront.user.bpms;

import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.dismefront.user.User;
import org.dismefront.user.UserRepository;
import org.dismefront.user.role.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("registerUserDelegate")
public class RegisterUserDelegate implements JavaDelegate {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void execute(org.camunda.bpm.engine.delegate.DelegateExecution execution) throws Exception {
        String phoneNumber = (String) execution.getVariable("textfield_phoneNumber");
        String password = (String) execution.getVariable("textfield_mkebwc");

        System.out.println("Attempting to register user with phone number: " + phoneNumber);
        System.out.println(execution.getVariables());

        if (userRepository.findByPhoneNumber(phoneNumber).isPresent()) {
            execution.setVariable("registrationError", true);
        } else {
            User user = new User();
            user.setName(phoneNumber);
            user.setSurname(phoneNumber);
            user.setPhoneNumber(phoneNumber);
            user.setPasswordHash(passwordEncoder.encode(password));
            user.setUserRoles(Set.of(UserRole.STANDARD));
            userRepository.save(user);
            execution.setVariable("registrationError", false);
        }
    }
}
