package com.accesa.pricecomparator.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DiscountDTO {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Store ID is required")
    private Long storeId;

    @NotNull(message = "fromDate is required")
    private LocalDate fromDate;

    @NotNull(message = "toDate is required")
    private LocalDate toDate;

    @NotNull(message = "Percentage of discount is required")
    @Min(value = 1, message = "Minimum discount is 1%")
    @Max(value = 100, message = "Maximum discount is 100%")
    private Double percentage;
}
