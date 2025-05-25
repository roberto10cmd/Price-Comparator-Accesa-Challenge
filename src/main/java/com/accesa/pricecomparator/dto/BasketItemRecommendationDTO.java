package com.accesa.pricecomparator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketItemRecommendationDTO {

    private Long productId;
    private String productName;
    private String brand;

    private String storeName;           // Magazinul ales (
    private double originalPrice;       // pretul de baza (fără reducere)
    private double discountedPrice;     // pretul final cu reducere, daca exista
    private Double discountPercentage;  // procent reducere sau null
    private boolean discountApplied;    // true dacă a fost aplicată o reducere

    private List<StorePriceDTO> allPrices; // Variantele de preț din toate magazinele
}
