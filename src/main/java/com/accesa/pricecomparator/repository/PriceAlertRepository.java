package com.accesa.pricecomparator.repository;

import com.accesa.pricecomparator.model.PriceAlert;
import com.accesa.pricecomparator.model.Product;
import com.accesa.pricecomparator.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {
    List<PriceAlert> findByProductAndActiveTrue(Product product);
    boolean existsByUserAndProductAndActiveTrue(User user, Product product);
    List<PriceAlert> findByUserAndProductAndActiveTrue(User user, Product product);

}
