package com.gkp.onlinebookstore.service;

import java.util.List;

import com.gkp.onlinebookstore.dto.BookCategoryDTO;
import com.gkp.onlinebookstore.dto.BookDTO;
import com.gkp.onlinebookstore.entity.Book;
import com.gkp.onlinebookstore.entity.BookCategory;

public interface AdminService {

	    // Add BookCategory
	    BookCategory addCategory(BookCategoryDTO bookCategoryDTO);

	    // Get All BookCategories
	    List<BookCategory> getAllBookCategories();

	    // Update BookCategory
	    BookCategory updateBookCategory(Long id, BookCategoryDTO bookCategoryDTO);

	    // Delete BookCategory
	    boolean deleteBookCategory(Long id);

	    // Add Single Book
	    Book saveBook(BookDTO bookDTO);
	    
	    //Add Multiple Books
	    List<BookDTO> saveBooks(List<BookDTO> bookDTOs);


	    // Get All Books
	    List<BookDTO> getBooks();

	    // Add Books to Category
	    BookCategory addBooksToCategory(String categoryName, List<BookDTO> bookDTOs);
	}


