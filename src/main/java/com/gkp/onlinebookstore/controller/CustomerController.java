package com.gkp.onlinebookstore.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.gkp.onlinebookstore.dto.BookDTO;
import com.gkp.onlinebookstore.dto.CartDTO;
import com.gkp.onlinebookstore.dto.OrderDTO;
import com.gkp.onlinebookstore.dto.OrderResponseDTO;
import com.gkp.onlinebookstore.entity.BookCategory;
import com.gkp.onlinebookstore.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@PreAuthorize("hasRole('Customer')")
@RequiredArgsConstructor
public class CustomerController {

   
    private final CustomerService customerService;
    

    @GetMapping("/viewBooks")
	public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = customerService.getBooks();
        return ResponseEntity.ok(books);
    }

    
    

    // Add book to cart
    @PostMapping("/{customerId}/cart/{bookId}")
    public ResponseEntity<CartDTO> addBookToCart(
            @PathVariable Long customerId,
            @PathVariable Long bookId,
            @RequestParam int quantity) {
        CartDTO cartDTO = customerService.addBookToCart(customerId, bookId, quantity);
        return ResponseEntity.ok(cartDTO);
    }

    // View cart items
    @GetMapping("/{customerId}/cart")
    public ResponseEntity<CartDTO> viewCartItems(@PathVariable Long customerId) {
        CartDTO cartDTO = customerService.viewCartItems(customerId);
        return ResponseEntity.ok(cartDTO);
    }
    
    //Remove From the Cart 
    @DeleteMapping("/{customerId}/cart/{bookId}")
    public ResponseEntity<String> removeBookFromCart(
            @PathVariable Long customerId,
            @PathVariable Long bookId) {
        customerService.removeCartItem(customerId, bookId);
        return ResponseEntity.ok("Item successfully removed from the cart!");
    }

    
    
    

    // Place an order
    @PostMapping("/{customerId}/order")
    public ResponseEntity<OrderResponseDTO> placeOrder(
            @PathVariable Long customerId,
            @RequestParam String address) {
        OrderDTO orderDTO = customerService.placeOrder(customerId, address);
        OrderResponseDTO response=new OrderResponseDTO("Order Placed Successfully", orderDTO);
        return ResponseEntity.ok(response);
    }
    
    
}
