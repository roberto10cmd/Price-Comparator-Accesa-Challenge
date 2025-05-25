package com.accesa.pricecomparator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketOptimizationDTO {

    private Long basketId;
    private List<BasketItemRecommendationDTO> recommendations;
    private double totalPrice;
}
