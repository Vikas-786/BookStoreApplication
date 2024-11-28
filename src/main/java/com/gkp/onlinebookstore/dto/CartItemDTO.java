package com.gkp.onlinebookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
	private Long id;
    private Long bookId;      
    private String bookTitle; 
    private int quantity;     
    private double price;     
    private double subtotal;  
}
