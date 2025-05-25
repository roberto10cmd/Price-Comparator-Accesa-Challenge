package com.accesa.pricecomparator.dto;

import lombok.Data;

@Data
public class PriceAlertRequest {
    private Long userId;
    private Long productId;
    private Double targetPrice;
}
