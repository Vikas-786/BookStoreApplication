package com.gkp.onlinebookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BookDTO {
    private Long id;
    private String title;
    private String language;
    private Integer popularity;
    private String productsku;
    private Double price;
    private Double discountPercentage;
    private Double finalPrice;
    private String description;
    private String categoryName; 
	private String authorName; 
	private AdditionalInfomationDTO additionalInfomationDTO; 
	 
   }
