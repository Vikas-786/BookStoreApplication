package com.gkp.onlinebookstore.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.gkp.onlinebookstore.dto.BookCategoryDTO;
import com.gkp.onlinebookstore.dto.BookDTO;
import com.gkp.onlinebookstore.entity.Book;
import com.gkp.onlinebookstore.entity.BookCategory;
import com.gkp.onlinebookstore.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('Admin')")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

	// Add a new book category
	@PostMapping("/addCategories")
	public ResponseEntity<BookCategory> addCategory(@RequestBody BookCategoryDTO bookCategoryDTO) {
		BookCategory category = adminService.addCategory(bookCategoryDTO);
		return ResponseEntity.ok(category);
	}

	// Get all book categories
	@GetMapping("/viewCategories")
	public ResponseEntity<List<BookCategory>> getAllCategories() {
		List<BookCategory> categories = adminService.getAllBookCategories();
		return ResponseEntity.ok(categories);
	}

	// Update an existing book category
	@PutMapping("/categories/{id}")
	public ResponseEntity<BookCategory> updateCategory(@PathVariable Long id,
			@RequestBody BookCategoryDTO bookCategoryDTO) {
		BookCategory updatedCategory = adminService.updateBookCategory(id, bookCategoryDTO);
		return ResponseEntity.ok(updatedCategory);
	}

	// Delete a book category
	@DeleteMapping("/deleteCategory/{id}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
		boolean deleted = adminService.deleteBookCategory(id);
		if (deleted) {
			return ResponseEntity.ok("Category deleted successfully");
		} else {
			return ResponseEntity.status(404).body("Category not found");
		}
	}

	// Add a new book
	@PostMapping("/addBook")
	public ResponseEntity<Book> saveBook(@RequestBody BookDTO bookDTO) {
		Book savedBook = adminService.saveBook(bookDTO);
		return ResponseEntity.ok(savedBook);
	}

	// Add multiple books
	@PostMapping("/addBooks")
	public ResponseEntity<List<BookDTO>> saveBooks(@RequestBody List<BookDTO> bookDTOs) {
		List<BookDTO> savedBooks = adminService.saveBooks(bookDTOs);
		return ResponseEntity.ok(savedBooks);
	}

	// Get all books
	@GetMapping("/viewBooks")
	public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = adminService.getBooks();
        return ResponseEntity.ok(books);
    }

	// Add books to a category
	@PostMapping("/categories/{categoryName}/books")
	public ResponseEntity<BookCategory> addBooksToCategory(@PathVariable String categoryName,
			@RequestBody List<BookDTO> bookDTOs) {
		BookCategory updatedCategory = adminService.addBooksToCategory(categoryName, bookDTOs);
		return ResponseEntity.ok(updatedCategory);
	}
	
	//
}
