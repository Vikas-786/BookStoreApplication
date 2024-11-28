package com.gkp.onlinebookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gkp.onlinebookstore.entity.BookCategory;

import java.util.List;
import java.util.Optional;


public interface BookCategoryRespository extends JpaRepository<BookCategory, Long> {
	
	Optional<BookCategory> findByName(String name);

	
	
}
