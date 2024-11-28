package com.gkp.onlinebookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
	
	private String message;
    private OrderDTO orderDetails;

}
