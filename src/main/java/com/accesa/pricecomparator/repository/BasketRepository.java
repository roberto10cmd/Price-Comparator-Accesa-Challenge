package com.accesa.pricecomparator.repository;

import com.accesa.pricecomparator.model.Basket;
import com.accesa.pricecomparator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    List<Basket> findByUser(User user);
    Optional<Basket> findTopByUserOrderByCreatedAtDesc(User user);

}
