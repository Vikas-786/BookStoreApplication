package com.gkp.onlinebookstore.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class OrderDTO {
	@NonNull
	private Long orderId;
	
	@NonNull
    private Double totalAmount;
	
	@NonNull
    private String status;
	
	@NonNull
    private String address;
	
	
}
