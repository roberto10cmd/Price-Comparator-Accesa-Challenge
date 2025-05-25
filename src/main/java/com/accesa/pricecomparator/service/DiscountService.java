package com.accesa.pricecomparator.service;

import com.accesa.pricecomparator.dto.DiscountDTO;
import com.accesa.pricecomparator.dto.DiscountProductDTO;
import com.accesa.pricecomparator.model.Discount;
import com.accesa.pricecomparator.model.PriceHistory;
import com.accesa.pricecomparator.model.Product;
import com.accesa.pricecomparator.model.Store;
import com.accesa.pricecomparator.repository.DiscountRepository;
import com.accesa.pricecomparator.repository.PriceHistoryRepository;
import com.accesa.pricecomparator.repository.ProductRepository;
import com.accesa.pricecomparator.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final PriceHistoryRepository priceHistoryRepository ;
    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    public Optional<Discount> getDiscountById(Long id) {
        return discountRepository.findById(id);
    }

    public Discount createDiscount(DiscountDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + dto.getProductId()));

        Store store = storeRepository.findById(dto.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + dto.getStoreId()));

        Discount discount = new Discount();
        discount.setProduct(product);
        discount.setStore(store);
        discount.setFromDate(dto.getFromDate());
        discount.setToDate(dto.getToDate());
        discount.setPercentage(dto.getPercentage());

        return discountRepository.save(discount);
    }

    public void deleteDiscount(Long id) {
        discountRepository.deleteById(id);
    }

    private double round2Decimals(double value) {
    return Math.round(value * 100.0) / 100.0;
}


    public Map<String, List<DiscountProductDTO>> getTop3DiscountsPerStore() {
    
        // Aici putem alege intre discounturile care sunt valide SAU discounturile care au fost overall aplicate

        // doar discounturi active
        List<Discount> discounts = discountRepository
        .findByFromDateLessThanEqualAndToDateGreaterThanEqual(LocalDate.now(), LocalDate.now());

        //toate discounturile ( all )
       // List<Discount> discounts = discountRepository.findAll(); 


    Map<String, List<DiscountProductDTO>> groupedByStore = new HashMap<>();


    for (Discount d : discounts) {
        PriceHistory latestPrice = priceHistoryRepository
            .findTopByProductAndStoreOrderByDateDesc(d.getProduct(), d.getStore())
            .orElse(null);
        if (latestPrice == null) continue;

        double discountedPrice = latestPrice.getPrice() * (1 - d.getPercentage() / 100.0);

        DiscountProductDTO dto = new DiscountProductDTO(
            d.getProduct().getId(),
            d.getProduct().getName() + " (" + d.getProduct().getBrand() + ")",
            d.getStore().getName(),
            d.getPercentage(),
            d.getFromDate() + " → " + d.getToDate(),
            round2Decimals(discountedPrice)
        );

        String storeName = d.getStore().getName();
        groupedByStore.computeIfAbsent(storeName, k -> new ArrayList<>()).add(dto);
    }

    // pastram doar top 3 pe fiecare magazin
    for (Map.Entry<String, List<DiscountProductDTO>> entry : groupedByStore.entrySet()) {
        List<DiscountProductDTO> top3 = entry.getValue().stream()
            .sorted(Comparator.comparing(DiscountProductDTO::getPercentage).reversed())
            .limit(3)
            .toList();
        groupedByStore.put(entry.getKey(), top3);
    }

    return groupedByStore;
}




// functia care cauta si returneaza cele mai noi discounturi in urma cu " daysback " zile 
    public List<DiscountProductDTO> getNewDiscounts(int daysBack) {
    LocalDate threshold = LocalDate.now().minusDays(daysBack);
    List<Discount> recent = discountRepository.findByFromDateGreaterThanEqual(threshold);

    List<DiscountProductDTO> result = new ArrayList<>();

    for (Discount d : recent) {
        PriceHistory latestPrice = priceHistoryRepository
                .findTopByProductAndStoreOrderByDateDesc(d.getProduct(), d.getStore())
                .orElse(null);
        if (latestPrice == null) continue;

        double discountedPrice = latestPrice.getPrice() * (1 - d.getPercentage() / 100);

        result.add(new DiscountProductDTO(
            d.getId(),
            d.getProduct().getName() + " (" + d.getProduct().getBrand() + ")",
            d.getStore().getName(),
            d.getPercentage(),
            d.getFromDate() + " → " + d.getToDate(),
            discountedPrice
        ));
    }

    return result;
}

}
