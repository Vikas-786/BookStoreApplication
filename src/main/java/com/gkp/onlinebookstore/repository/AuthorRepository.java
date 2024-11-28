package com.gkp.onlinebookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gkp.onlinebookstore.entity.Author;
import com.gkp.onlinebookstore.entity.BookCategory;

import java.util.*;


public interface AuthorRepository extends JpaRepository<Author, Long> {
	
	List<Author> findByCategory(BookCategory category);
	
	 Optional<Author> findByName(String name);
	

}
