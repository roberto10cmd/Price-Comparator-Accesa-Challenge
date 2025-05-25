package com.accesa.pricecomparator.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "discounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @NotNull(message = "fromDate is required")
    private LocalDate fromDate;

    @NotNull(message = "toDate is required")
    private LocalDate toDate;

    @NotNull(message = "percentageOfDiscount is required")
    @Min(value = 1, message = "Minimum discount is 1%")
    @Max(value = 100, message = "Maximum discount is 100%")
    private Double percentage;
}
