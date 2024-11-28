package com.gkp.onlinebookstore.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gkp.onlinebookstore.dto.AdditionalInfomationDTO;
import com.gkp.onlinebookstore.dto.BookCategoryDTO;
import com.gkp.onlinebookstore.dto.BookDTO;
import com.gkp.onlinebookstore.entity.AdditionalInformation;
import com.gkp.onlinebookstore.entity.Author;
import com.gkp.onlinebookstore.entity.Book;
import com.gkp.onlinebookstore.entity.BookCategory;
import com.gkp.onlinebookstore.repository.AdditionalInfoRepo;
import com.gkp.onlinebookstore.repository.AuthorRepository;
import com.gkp.onlinebookstore.repository.BookCategoryRespository;
import com.gkp.onlinebookstore.repository.BookRepository;
import com.gkp.onlinebookstore.service.AdminService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final AuthorRepository authorRepository;
	private final BookCategoryRespository bookCategoryRespository;
	private final BookRepository bookRepository;
	private final AdditionalInfoRepo additionalInfoRepo;

	@Override
	public BookCategory addCategory(BookCategoryDTO bookCategoryDTO) {
		BookCategory category = new BookCategory();
		category.setName(bookCategoryDTO.getName());
		return bookCategoryRespository.save(category);
	}

	@Override
	public List<BookCategory> getAllBookCategories() {
		return bookCategoryRespository.findAll();
	}

	@Override
	public BookCategory updateBookCategory(Long id, BookCategoryDTO bookCategoryDTO) {
		Optional<BookCategory> optionalCategory = bookCategoryRespository.findById(id);
		if (optionalCategory.isPresent()) {
			BookCategory category = optionalCategory.get();
			category.setName(bookCategoryDTO.getName());
			return bookCategoryRespository.save(category);
		}
		return null;
	}

	@Override
	public boolean deleteBookCategory(Long id) {
		Optional<BookCategory> optionalCategory = bookCategoryRespository.findById(id);
		if (optionalCategory.isPresent()) {
			bookCategoryRespository.delete(optionalCategory.get());
			return true;
		}
		return false;
	}

	@Override
	public Book saveBook(BookDTO bookDTO) {
		Book book = new Book();
		book.setTitle(bookDTO.getTitle());
		book.setLanguage(bookDTO.getLanguage());
		book.setDescription(bookDTO.getDescription());
		book.setPrice(bookDTO.getPrice());

		Author author = authorRepository.findByName(bookDTO.getAuthorName()).orElseGet(() -> {
			Author newAuthor = new Author();
			newAuthor.setName(bookDTO.getAuthorName());
			return authorRepository.save(newAuthor);
		});
		book.setAuthor(author);

		BookCategory category = bookCategoryRespository.findByName(bookDTO.getCategoryName()).orElseGet(() -> {
			BookCategory newCategory = new BookCategory();
			newCategory.setName(bookDTO.getCategoryName());
			return bookCategoryRespository.save(newCategory);
		});
		book.setCategory(category);

		if (bookDTO.getAdditionalInfomationDTO() != null) {
			AdditionalInformation additionalInfo = new AdditionalInformation();
			AdditionalInfomationDTO infoDTO = bookDTO.getAdditionalInfomationDTO();
			additionalInfo.setWeight(infoDTO.getWeight());
			additionalInfo.setDimensions(infoDTO.getDimensions());
			additionalInfo.setPublisher(infoDTO.getPublisher());
			additionalInfo.setLanguage(infoDTO.getLanguage());
			additionalInfo.setPublishingYear(infoDTO.getPublishingYear());
			additionalInfo.setTotalPages(infoDTO.getTotalPages());
			additionalInfo.setEdition(infoDTO.getEdition());
			additionalInfo.setBuyFromAmazonLink(infoDTO.getBuyFromAmazonLink());
			additionalInfo = additionalInfoRepo.save(additionalInfo);
			book.setAdditionalInformation(additionalInfo);
		}

		return bookRepository.save(book);
	}

	@Override
	public List<BookDTO> saveBooks(List<BookDTO> bookDTOs) {
		List<Book> books = new ArrayList<>();

		for (BookDTO bookDTO : bookDTOs) {
			// Map BookDTO to Book entity
			Book book = new Book();
			book.setId(bookDTO.getId());
			book.setTitle(bookDTO.getTitle());
			book.setProductsku(bookDTO.getProductsku());
			book.setLanguage(bookDTO.getLanguage());
			book.setDescription(bookDTO.getDescription());
			book.setDiscountPercentage(bookDTO.getDiscountPercentage());
			book.setPrice(bookDTO.getPrice());
			book.setFinalPrice(bookDTO.getFinalPrice());

			// Handle Author dynamically
			Author author = authorRepository.findByName(bookDTO.getAuthorName()).orElseGet(() -> {
				Author newAuthor = new Author();
				newAuthor.setName(bookDTO.getAuthorName());
				return authorRepository.save(newAuthor);
			});
			book.setAuthor(author);

			// Handle Category dynamically
			BookCategory category = bookCategoryRespository.findByName(bookDTO.getCategoryName()).orElseGet(() -> {
				BookCategory newCategory = new BookCategory();
				newCategory.setName(bookDTO.getCategoryName());
				return bookCategoryRespository.save(newCategory);
			});
			book.setCategory(category);

			// Handle Additional Information dynamically

			if (bookDTO.getAdditionalInfomationDTO() != null) {
				AdditionalInfomationDTO additionalInfoDTO = bookDTO.getAdditionalInfomationDTO();

				AdditionalInformation additionalInfo = new AdditionalInformation();
				additionalInfo.setId(additionalInfoDTO.getId());
				additionalInfo.setWeight(additionalInfoDTO.getWeight());
				additionalInfo.setDimensions(additionalInfoDTO.getDimensions());
				additionalInfo.setPublisher(additionalInfoDTO.getPublisher());
				additionalInfo.setLanguage(additionalInfoDTO.getLanguage());
				additionalInfo.setIsbn(additionalInfoDTO.getIsbn());
				additionalInfo.setPublishingMonth(additionalInfoDTO.getPublishingMonth());
				additionalInfo.setPublishingYear(additionalInfoDTO.getPublishingYear());
				additionalInfo.setTotalPages(additionalInfoDTO.getTotalPages());
				additionalInfo.setEdition(additionalInfoDTO.getEdition());
				additionalInfo.setBuyFromAmazonLink(additionalInfoDTO.getBuyFromAmazonLink());

				// Save Additional Information entity and set it in Book
				additionalInfo = additionalInfoRepo.save(additionalInfo);
				book.setAdditionalInformation(additionalInfo);
			}

			books.add(book);

		}
		// Save all books in a single transaction
		books = bookRepository.saveAll(books);

		// Convert saved books to DTOs and return
		return books.stream().map(book -> {
			BookDTO dto = new BookDTO();
			dto.setId(book.getId());
			dto.setTitle(book.getTitle());
			dto.setLanguage(book.getLanguage());
			dto.setDescription(book.getDescription());
			dto.setProductsku(book.getProductsku());
			dto.setPrice(book.getPrice());
			dto.setDiscountPercentage(book.getDiscountPercentage());
			dto.setFinalPrice(book.getFinalPrice());
			dto.setAuthorName(book.getAuthor().getName());
			dto.setCategoryName(book.getCategory().getName());

			// Map Additional Information DTO
			if (book.getAdditionalInformation() != null) {
			
				AdditionalInfomationDTO additionalInfoDTO = new AdditionalInfomationDTO();
				additionalInfoDTO.setId(book.getAdditionalInformation().getId());
				additionalInfoDTO.setWeight(book.getAdditionalInformation().getWeight());
				additionalInfoDTO.setDimensions(book.getAdditionalInformation().getDimensions());
				additionalInfoDTO.setPublisher(book.getAdditionalInformation().getPublisher());
				additionalInfoDTO.setLanguage(book.getAdditionalInformation().getLanguage());
				additionalInfoDTO.setIsbn(book.getAdditionalInformation().getIsbn());
				additionalInfoDTO.setPublishingMonth(book.getAdditionalInformation().getPublishingMonth());
				additionalInfoDTO.setPublishingYear(book.getAdditionalInformation().getPublishingYear());
				additionalInfoDTO.setTotalPages(book.getAdditionalInformation().getTotalPages());
				additionalInfoDTO.setEdition(book.getAdditionalInformation().getEdition());
				additionalInfoDTO.setBuyFromAmazonLink(book.getAdditionalInformation().getBuyFromAmazonLink());
				dto.setAdditionalInfomationDTO(additionalInfoDTO);
			}

			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public List<BookDTO> getBooks() {
		// Fetch all books from the database
		List<Book> books = bookRepository.findAll();

		// Convert books to BookDTOs and return
		return books.stream().map(book -> {
			BookDTO dto = new BookDTO();
			dto.setId(book.getId());
			dto.setPopularity(book.getPopularity());
			dto.setProductsku(book.getProductsku());
			dto.setDiscountPercentage(book.getDiscountPercentage());
			dto.setTitle(book.getTitle());
			dto.setLanguage(book.getLanguage());
			dto.setDescription(book.getDescription());
			dto.setPrice(book.getPrice());
			dto.setFinalPrice(book.getFinalPrice());
			dto.setAuthorName(book.getAuthor().getName());
			dto.setCategoryName(book.getCategory().getName());

			// Map Additional Information DTO
			if (book.getAdditionalInformation() != null) {
				AdditionalInfomationDTO additionalInfoDTO = new AdditionalInfomationDTO();
				additionalInfoDTO.setId(book.getAdditionalInformation().getId());
				additionalInfoDTO.setWeight(book.getAdditionalInformation().getWeight());
				additionalInfoDTO.setDimensions(book.getAdditionalInformation().getDimensions());
				additionalInfoDTO.setPublisher(book.getAdditionalInformation().getPublisher());
				additionalInfoDTO.setLanguage(book.getAdditionalInformation().getLanguage());
				additionalInfoDTO.setPublishingYear(book.getAdditionalInformation().getPublishingYear());
				additionalInfoDTO.setTotalPages(book.getAdditionalInformation().getTotalPages());
				additionalInfoDTO.setEdition(book.getAdditionalInformation().getEdition());
				additionalInfoDTO.setBuyFromAmazonLink(book.getAdditionalInformation().getBuyFromAmazonLink());
				dto.setAdditionalInfomationDTO(additionalInfoDTO);
			}

			return dto;
		}).collect(Collectors.toList());
	}

	
	
	@Override
	public BookCategory addBooksToCategory(String categoryName, List<BookDTO> bookDTOs) {
		BookCategory bookCategory = bookCategoryRespository.findByName(categoryName)
				.orElseThrow(() -> new RuntimeException("Category Not Found: " + categoryName));
		for (BookDTO bookDTO : bookDTOs) {
			Book book = saveBook(bookDTO);
			bookCategory.getBooks().add(book);
		}
		return bookCategoryRespository.save(bookCategory);
	}

}
