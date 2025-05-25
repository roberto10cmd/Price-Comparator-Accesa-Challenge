package com.accesa.pricecomparator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BasketDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Basket name is required")
    private String name;
}
