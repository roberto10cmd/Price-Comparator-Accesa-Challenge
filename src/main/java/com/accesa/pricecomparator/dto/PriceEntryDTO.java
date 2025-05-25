package com.accesa.pricecomparator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PriceEntryDTO {
    private LocalDate date;      
    private double price;        
    private String storeName;    
    private String productName; 
    private String brand;
}
