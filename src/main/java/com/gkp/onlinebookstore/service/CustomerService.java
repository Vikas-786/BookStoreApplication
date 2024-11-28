package com.gkp.onlinebookstore.service;

import java.util.List;

import com.gkp.onlinebookstore.dto.BookDTO;
import com.gkp.onlinebookstore.dto.CartDTO;
import com.gkp.onlinebookstore.dto.OrderDTO;
import com.gkp.onlinebookstore.entity.BookCategory;

public interface CustomerService {
	

	 List<BookDTO> getBooks();
	
	CartDTO addBookToCart(Long customerId, Long bookId, int quantity);

    CartDTO viewCartItems(Long customerId);

    CartDTO removeCartItem(Long customerId, Long bookId);

    OrderDTO placeOrder(Long customerId, String address);
    
    //Payment Processing - Update Here 
}
