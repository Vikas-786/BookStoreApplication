package com.gkp.onlinebookstore.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
	
	private Long id;
    private Long customerId; 
    private List<CartItemDTO> cartItems; 
    private Double subtotal; 
    private Double total;    
}
