package com.gkp.onlinebookstore.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gkp.onlinebookstore.dto.AdditionalInfomationDTO;
import com.gkp.onlinebookstore.dto.BookDTO;
import com.gkp.onlinebookstore.dto.CartDTO;
import com.gkp.onlinebookstore.dto.CartItemDTO;
import com.gkp.onlinebookstore.dto.OrderDTO;
import com.gkp.onlinebookstore.entity.Book;
import com.gkp.onlinebookstore.entity.BookCategory;
import com.gkp.onlinebookstore.entity.Cart;
import com.gkp.onlinebookstore.entity.CartItem;
import com.gkp.onlinebookstore.entity.Customer;
import com.gkp.onlinebookstore.entity.Order;
import com.gkp.onlinebookstore.repository.BookCategoryRespository;
import com.gkp.onlinebookstore.repository.BookRepository;
import com.gkp.onlinebookstore.repository.CartRepository;
import com.gkp.onlinebookstore.repository.CustomerRepository;
import com.gkp.onlinebookstore.repository.OrderRepository;
import com.gkp.onlinebookstore.service.CustomerService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository customerRepository;
	private final BookRepository bookRepository;
	private final CartRepository cartRepository;
	private final OrderRepository orderRepository;
	
	
	
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
	@Transactional
	public CartDTO addBookToCart(Long customerId, Long bookId, int quantity) {
		// Fetch Customer
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

		// Fetch Book
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

		// Fetch or create Cart
		Cart cart = customer.getCart();
		if (cart == null) {
			cart = new Cart();
			cart.setCustomer(customer);
		}

		// Check if book already exists in the cart
		Optional<CartItem> existingCartItem = cart.getCartItems().stream()
				.filter(item -> item.getBook().getId().equals(bookId)).findFirst();

		if (existingCartItem.isPresent()) {
			// Update quantity and price
			CartItem cartItem = existingCartItem.get();
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
		} else {
			// Add new CartItem
			CartItem newCartItem = new CartItem();
			newCartItem.setBook(book);
			newCartItem.setCart(cart);
			newCartItem.setQuantity(quantity);
			newCartItem.setPrice(book.getFinalPrice()); // Assuming final price is set in Book entity
			cart.getCartItems().add(newCartItem);
		}

		// Recalculate totals
		cart.calculateTotals();

		// Save Cart
		Cart savedCart = cartRepository.save(cart);

		// Convert to DTO and return
		return convertCartToDTO(savedCart);
	}

	private CartDTO convertCartToDTO(Cart cart) {
		CartDTO cartDTO = new CartDTO();
		cartDTO.setId(cart.getId());
		cartDTO.setCustomerId(cart.getCustomer().getId());
		cartDTO.setSubtotal(cart.getSubtotal());
		cartDTO.setTotal(cart.getTotal());
		cartDTO.setCartItems(cart.getCartItems().stream().map(this::convertCartItemToDTO).collect(Collectors.toList()));
		return cartDTO;
	}

	private CartItemDTO convertCartItemToDTO(CartItem cartItem) {
		CartItemDTO cartItemDTO = new CartItemDTO();
		cartItemDTO.setId(cartItem.getId());
		cartItemDTO.setBookId(cartItem.getBook().getId());
		cartItemDTO.setBookTitle(cartItem.getBook().getTitle());
		cartItemDTO.setQuantity(cartItem.getQuantity());
		cartItemDTO.setPrice(cartItem.getPrice());
		cartItemDTO.setSubtotal(cartItem.getSubtotal());
		return cartItemDTO;
	}

	// - View Cart
	public CartDTO viewCartItems(Long customerId) {
		// Find customer by ID
		Optional<Customer> customerOptional = customerRepository.findById(customerId);

		if (customerOptional.isPresent()) {
			Customer customer = customerOptional.get();
			Cart cart = customer.getCart();

			if (cart != null) {
				// Map CartItems to CartItemDTOs
				List<CartItemDTO> cartItemDTOs = cart.getCartItems().stream()
						.map(cartItem -> new CartItemDTO(cartItem.getId(), cartItem.getBook().getId(), // Assuming Book
																										// entity has an
																										// ID
								cartItem.getBook().getTitle(), // Assuming Book entity has a title
								cartItem.getQuantity(), cartItem.getPrice(),
								cartItem.getQuantity() * cartItem.getPrice() // Calculate subtotal
						)).collect(Collectors.toList());

				// Return a CartDTO with the CartItemDTO list
				return new CartDTO(cart.getId(), cart.getCustomer().getId(), cartItemDTOs, cart.getSubtotal(),
						cart.getTotal());
			}
		}

		// Return an empty CartDTO if cart or customer is not found
		return new CartDTO();
	}

	//Removing from cart 
	@Override
	@Transactional
	public CartDTO removeCartItem(Long customerId, Long bookId) {
	    // Fetch the customer
	    Customer customer = customerRepository.findById(customerId)
	            .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

	    // Fetch the customer's cart
	    Cart cart = customer.getCart();
	    if (cart == null || cart.getCartItems().isEmpty()) {
	        throw new IllegalStateException("Cart is empty, no items to remove");
	    }

	    // Find the cart item by bookId
	    Optional<CartItem> cartItemOptional = cart.getCartItems().stream()
	            .filter(item -> item.getBook().getId().equals(bookId))
	            .findFirst();

	    if (cartItemOptional.isEmpty()) {
	        throw new IllegalArgumentException("Book not found in the cart with id: " + bookId);
	    }

	    // Remove the cart item
	    CartItem cartItemToRemove = cartItemOptional.get();
	    cart.getCartItems().remove(cartItemToRemove);

	    // Recalculate cart totals
	    cart.calculateTotals();

	    // Save the updated cart
	    Cart updatedCart = cartRepository.save(cart);

	    // Return updated CartDTO
	    return convertCartToDTO(updatedCart);
	}

	
// OrderService : - Placing an order 

	@Override
	public OrderDTO placeOrder(Long customerId, String address) {

		// first check whether customer exist or not
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found with id:" + customerId));

		// fetch the cart object from customer
		Cart cart = customer.getCart();

		if (cart == null || cart.getCartItems().isEmpty()) {
			throw new IllegalStateException("Cart is empty, cannot place order");
		}

		List<CartItem> cartItems = cart.getCartItems();

		// calculate the total amount
		double totalAmount = cartItems.stream().mapToDouble(CartItem::getSubtotal).sum();

		// create new order
		Order order = new Order();
		order.setCustomer(customer);
		order.setTotalAmount(totalAmount);
		order.setStatus("Placed");
		order.setAddress(address);

		// Map CartItems to Books for Order
		order.setBooks(cartItems.stream().map(CartItem::getBook).collect(Collectors.toList()));

		// save the order
		orderRepository.save(order);

		// clearing the cart once order is placed
		cart.getCartItems().clear();
		cartRepository.save(cart);

		return new OrderDTO(order.getId(), totalAmount, order.getStatus(), address);

	}

	

}
