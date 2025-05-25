package com.accesa.pricecomparator.repository;

import com.accesa.pricecomparator.model.Discount;
import com.accesa.pricecomparator.model.Product;
import com.accesa.pricecomparator.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByStore(Store store);
    List<Discount> findByProduct(Product product);
    List<Discount> findByFromDateGreaterThanEqual(LocalDate date);
    Optional<Discount> findByProductAndStoreAndFromDateLessThanEqualAndToDateGreaterThanEqual(
        Product product, Store store, LocalDate fromDate, LocalDate toDate
    );
    @Query("SELECT d FROM Discount d ORDER BY d.fromDate DESC")
    List<Discount> findLatestDiscounts();
    List<Discount> findByFromDateLessThanEqualAndToDateGreaterThanEqual(LocalDate from, LocalDate to);

}
