package com.gkp.onlinebookstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gkp.onlinebookstore.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {	
	Optional<User> findByEmail(String email);
	Optional <User> findByUsername(String username);
	
}
