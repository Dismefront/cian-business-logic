package org.dismefront.user;

import jakarta.servlet.http.HttpServletRequest;
import org.dismefront.user.dto.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    private void authenticate(String username, String password, HttpServletRequest request) throws AuthenticationException {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                username, password));
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authentication);
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody UserData req, HttpServletRequest request) {
        try {
            User user = new User();
            user.setName(req.getName());
            user.setSurname(req.getSurname());
            user.setPhoneNumber(req.getPhoneNumber());
            user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
            userRepository.save(user);
            authenticate(req.getPhoneNumber(), req.getPassword(), request);
            return ResponseEntity.ok(user);
        }
        catch (TransactionSystemException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The user with these credentials already exists");
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserData loginRequest, HttpServletRequest request) {
        try {
            authenticate(loginRequest.getPhoneNumber(), loginRequest.getPassword(), request);
            return ResponseEntity.ok().body("User logged in successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(403).body("User credentials are incorrect");
        }
    }
}
