package com.gkp.onlinebookstore.service;

import java.util.Optional;
import com.gkp.onlinebookstore.dto.AuthRequest;
import com.gkp.onlinebookstore.entity.PasswordResetToken;
import com.gkp.onlinebookstore.entity.User;

public interface UserService {
	
    User registerUser(User userobj);

    
    String login(AuthRequest authRequest);

    
    void createPasswordTokenForUser(User user);

    
    Optional<PasswordResetToken> findByToken(String token);

    
    void resetPassword(User user, String newPassword);

}
