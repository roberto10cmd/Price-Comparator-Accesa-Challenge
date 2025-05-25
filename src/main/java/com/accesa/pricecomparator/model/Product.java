package com.accesa.pricecomparator.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "External product ID is required")
    @Column(nullable = false)
    private String externalProductId;    // P001, P002 etc.

    @NotBlank(message = "Product name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Category is required")
    @Column(nullable = false)
    private String category;

    @NotBlank(message = "Brand is required")
    @Column(nullable = false)
    private String brand;

    @NotNull(message = "Package quantity is required")
    @Column(nullable = false)
    private Double packageQuantity;

    @NotBlank(message = "Package unit is required")
    @Column(nullable = false)
    private String packageUnit;          // kg, l, buc etc.

    @NotBlank(message = "Currency is required")
    @Column(nullable = false)
    private String currency;             // RON

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonBackReference
    private NutritionFacts nutritionFacts;

    public Product(String externalProductId, String name, String category, String brand,
                   Double packageQuantity, String packageUnit, String currency) {
        this.externalProductId = externalProductId;
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.packageQuantity = packageQuantity;
        this.packageUnit = packageUnit;
        this.currency = currency;
    }
}
