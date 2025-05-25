package com.accesa.pricecomparator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NutritionStatusDTO {
    private double saturatedFat;
    private String saturatedFatStatus;

    private double sugar;
    private String sugarStatus;

    private double protein;
}
