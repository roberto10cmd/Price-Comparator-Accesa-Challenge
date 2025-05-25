package com.accesa.pricecomparator.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "nutrition_facts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NutritionFacts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    @JsonManagedReference
    private Product product;

    @Column(nullable = true)
    private Double saturatedFatPer100g;

    @Column(nullable = true)
    private Double sugarPer100g;

    @Column(nullable = true)
    private Double proteinPer100g;
}
