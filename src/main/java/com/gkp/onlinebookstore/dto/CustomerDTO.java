package com.gkp.onlinebookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
	private Long id;
    private String name;
    private String email;
    private String address;
    private Long userId; 
}
