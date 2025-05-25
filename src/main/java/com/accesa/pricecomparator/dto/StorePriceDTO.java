package com.accesa.pricecomparator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StorePriceDTO {
    private String storeName;
    private double originalPrice;
    private double discountedPrice;
    private Double discountPercentage;
    private boolean discountApplied;

    private double valuePerUnit;
    private String unitLabel;

    private LocalDate date; 
}
