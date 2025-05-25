package com.accesa.pricecomparator.repository;

import com.accesa.pricecomparator.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByName(String name);
    Optional<Store> findByNameIgnoreCase(String name);

}
