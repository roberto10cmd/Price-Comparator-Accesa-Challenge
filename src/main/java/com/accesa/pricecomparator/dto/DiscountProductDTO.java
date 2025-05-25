package com.accesa.pricecomparator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiscountProductDTO {

    private Long productId ;
    private String productName;
    private String storeName;
    private double percentage;
    private String period;
    private double currentPrice; 
}
