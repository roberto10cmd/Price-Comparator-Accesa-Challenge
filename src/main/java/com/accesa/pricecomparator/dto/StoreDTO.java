package com.accesa.pricecomparator.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StoreDTO {
    @NotBlank(message = "Store name is required")
    private String name;
}
