package org.dismefront.user;

import jakarta.servlet.http.HttpServletRequest;
import org.dismefront.user.dto.UserData;
import org.dismefront.user.role.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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
            user.setUserRoles(Set.of(UserRole.STANDARD));
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

    @PutMapping("/grant/{userId}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity grantRole(@PathVariable Long userId, @RequestBody UserData data) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        try {
            user.getUserRoles().add(UserRole.valueOf(data.getRole()));
            userRepository.save(user);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role");
        }
    }

    @PutMapping("/revoke/{userId}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity revokeRole(@PathVariable Long userId, @RequestBody UserData data) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        try {
            user.getUserRoles().remove(UserRole.valueOf(data.getRole()));
            userRepository.save(user);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role");
        }
    }
}
