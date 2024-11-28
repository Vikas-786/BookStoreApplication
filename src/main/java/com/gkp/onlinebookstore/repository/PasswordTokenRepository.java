package com.gkp.onlinebookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gkp.onlinebookstore.entity.PasswordResetToken;
import com.gkp.onlinebookstore.entity.User;

import java.util.Optional;



@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, Long> {
			
				
			Optional<PasswordResetToken>findByToken(String token);
			
			void deleteByUser(User user); // to delete/invalidate 
	
}
