package com.gkp.onlinebookstore.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalInfomationDTO {
    private Long id;
    private Double weight;
    private String dimensions;
    private String publisher;
    private String language;
    private String isbn;
    private String publishingMonth;
    private Integer publishingYear;
    private Integer totalPages;
    private String edition;
    private String buyFromAmazonLink;
    
}
