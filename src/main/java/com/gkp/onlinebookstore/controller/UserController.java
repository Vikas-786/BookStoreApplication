package com.gkp.onlinebookstore.controller;

import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.gkp.onlinebookstore.dto.AuthRequest;
import com.gkp.onlinebookstore.dto.ForgetPasswordRequest;
import com.gkp.onlinebookstore.entity.PasswordResetToken;
import com.gkp.onlinebookstore.entity.User;
import com.gkp.onlinebookstore.repository.UserRepository;
import com.gkp.onlinebookstore.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	private final UserRepository userRepository;

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody User user) {
		userService.registerUser(user);
		return ResponseEntity.ok("Registration Successful. Check your email for Credentials");
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
		String token = userService.login(authRequest);
		return ResponseEntity.ok(token);

	}

	@PostMapping("/forget")
	public ResponseEntity<String> forgotPassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest) {
		String email = forgetPasswordRequest.getEmail();
		Optional<User> user = userRepository.findByEmail(email);
		if (user.isPresent()) {
			userService.createPasswordTokenForUser(user.get());
			return ResponseEntity.ok("Password Reset Link has been sent to your email");
		} else {
			return ResponseEntity.status(404).body("User not found");
		}

	}

	@PostMapping("/resetPassword")
	public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newpassword) {
		Optional<PasswordResetToken> passwordResetToken = userService.findByToken(token);
		if (passwordResetToken.isPresent()) {
			userService.resetPassword(passwordResetToken.get().getUser(), newpassword);
			return ResponseEntity.ok("Password Reset Successful");
		} else {
			return ResponseEntity.status(400).body("Invalid or Expired token");
		}
	}

}