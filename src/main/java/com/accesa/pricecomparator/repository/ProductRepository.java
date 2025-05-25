package com.accesa.pricecomparator.repository;

import com.accesa.pricecomparator.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByExternalProductId(String externalProductId);
    List<Product> findByCategory(String category);
    List<Product> findByBrand(String brand);
}
