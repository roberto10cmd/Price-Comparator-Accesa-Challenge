package com.accesa.pricecomparator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductDTO {

    @NotBlank(message = "External product ID is required")
    private String externalProductId;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotNull(message = "Package quantity is required")
    private Double packageQuantity;

    @NotBlank(message = "Package unit is required")
    private String packageUnit;

    @NotBlank(message = "Currency is required")
    private String currency;
}
