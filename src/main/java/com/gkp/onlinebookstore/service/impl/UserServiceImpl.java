package com.gkp.onlinebookstore.service.impl;

import java.util.Optional;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gkp.onlinebookstore.dto.AuthRequest;
import com.gkp.onlinebookstore.entity.Customer;
import com.gkp.onlinebookstore.entity.PasswordResetToken;
import com.gkp.onlinebookstore.entity.Role;
import com.gkp.onlinebookstore.entity.User;
import com.gkp.onlinebookstore.repository.CustomerRepository;
import com.gkp.onlinebookstore.repository.PasswordTokenRepository;
import com.gkp.onlinebookstore.repository.UserRepository;
import com.gkp.onlinebookstore.security.CustomUserDetails;
import com.gkp.onlinebookstore.security.CustomUserDetailsService;
import com.gkp.onlinebookstore.security.JwtUtil;
import com.gkp.onlinebookstore.service.EmailService;
import com.gkp.onlinebookstore.service.UserService;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	
    private final UserRepository userRepository;

    private final EmailService emailService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final PasswordTokenRepository passwordTokenRepository;

    private final CustomerRepository customerRepository;

    private final CustomUserDetailsService customUserDetailsService;

	@Override
    @Transactional
    public User registerUser(User userobj) {
        String email = userobj.getEmail();
        String username = email.substring(0, email.indexOf("@"));
        String password = UUID.randomUUID().toString();

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setEnabled(true);
        user.setRole(userobj.getRole());
        userRepository.save(user);

        // Save user as a customer if role is CUSTOMER
        if (user.getRole() == Role.Customer) {
            Customer customer = new Customer();
            customer.setName(username);
            customer.setEmail(email);
            customer.setAddress("");
            customerRepository.save(customer);
        }

        // Send registration email
        emailService.sendRegistrationEmail(email, username, password);
        return user;
    }

    @Override
    public String login(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials provided");
        }
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(authRequest.getUsername());
        String role = ((CustomUserDetails) userDetails).getRole().name();
        return jwtUtil.generateToken(userDetails.getUsername(), role);
    }

    @Override
    public void createPasswordTokenForUser(User user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user, LocalDateTime.now().plusMinutes(1));
        passwordTokenRepository.save(passwordResetToken);

        // Send reset email
        String resetLink = "http://localhost:8080/api/reset-password?token=" + token;
        emailService.sendEmail(user.getEmail(), "Password Reset Request",
                "To reset your password, click the link below:\n" + resetLink);
    }

    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        return passwordTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public void resetPassword(User user, String newPassword) {
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);
        passwordTokenRepository.deleteByUser(user);
    }

	

}
