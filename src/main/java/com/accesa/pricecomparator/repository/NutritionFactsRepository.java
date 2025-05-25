package com.accesa.pricecomparator.repository;

import com.accesa.pricecomparator.model.NutritionFacts;
import com.accesa.pricecomparator.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NutritionFactsRepository extends JpaRepository<NutritionFacts, Long> {
    Optional<NutritionFacts> findByProduct(Product product);
}
