package com.accesa.pricecomparator.repository;

import com.accesa.pricecomparator.model.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {
}
