package com.gkp.onlinebookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gkp.onlinebookstore.entity.Book;
import com.gkp.onlinebookstore.entity.BookCategory;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	
	public long countByCategory(BookCategory bookCategory);
	
	@Query("SELECT b FROM Book b JOIN b.category c WHERE c.name = :categoryName")
	List<Book> findBooksByCategoryName(@Param("categoryName") String categoryName);

	public Optional<List<Book>> findByTitle(String title);
	
}
