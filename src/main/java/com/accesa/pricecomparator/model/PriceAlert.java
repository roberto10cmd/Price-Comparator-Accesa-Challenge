package com.accesa.pricecomparator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "price_alert",
uniqueConstraints = @UniqueConstraint(columnNames = {"user_user_id", "product_id", "active"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Product product;

    @Column(nullable = false)
    private double targetPrice;

    private boolean active = true;
}
