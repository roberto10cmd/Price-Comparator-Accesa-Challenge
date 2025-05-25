package com.accesa.pricecomparator.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PriceHistoryDTO {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Store ID is required")
    private Long storeId;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be positive")
    private Double price;

    @NotNull(message = "Date is required")
    private LocalDate date; 
}
