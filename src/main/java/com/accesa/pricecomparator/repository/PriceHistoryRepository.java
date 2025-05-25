package com.accesa.pricecomparator.repository;

import com.accesa.pricecomparator.model.PriceHistory;
import com.accesa.pricecomparator.model.Product;
import com.accesa.pricecomparator.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long>,JpaSpecificationExecutor<PriceHistory> {
    List<PriceHistory> findByProduct(Product product);
    List<PriceHistory> findByStore(Store store);
    List<PriceHistory> findByDate(LocalDate date);
    List<PriceHistory> findByProductAndStore(Product product, Store store);
    List<PriceHistory> findByProductAndDate(Product product, LocalDate date);
    List<PriceHistory> findByProductAndStoreAndDate(Product product, Store store, LocalDate now);
    Optional<PriceHistory> findTopByProductOrderByDateDesc(Product product);
    Optional<PriceHistory> findTopByProductAndStoreOrderByDateDesc(Product product, Store store);
    Optional<PriceHistory> findTopByProductOrderByPriceAsc(Product product);
    Optional<PriceHistory> findTopByProductAndStoreOrderByPriceAsc(Product product, Store store);
    List<PriceHistory> findByProductOrderByDateDesc(Product product);
    List<PriceHistory> findByProductAndStoreOrderByDateDesc(Product product, Store store);

        @Query("""
        SELECT ph FROM PriceHistory ph
        WHERE ph.product = :product
        AND ph.date = (
            SELECT MAX(ph2.date)
            FROM PriceHistory ph2
            WHERE ph2.product = :product
            AND ph2.store = ph.store
            AND ph2.date <= CURRENT_DATE
        )
    """)
    List<PriceHistory> findLatestValidPerStore(@Param("product") Product product);

}
