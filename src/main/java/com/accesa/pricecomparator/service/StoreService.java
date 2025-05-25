package com.accesa.pricecomparator.service;

import com.accesa.pricecomparator.model.PriceHistory;
import com.accesa.pricecomparator.model.Product;
import com.accesa.pricecomparator.model.Store;
import com.accesa.pricecomparator.repository.StoreRepository;
import com.accesa.pricecomparator.repository.PriceHistoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final PriceHistoryRepository priceHistoryRepository;

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public Optional<Store> getStoreById(Long id) {
        return storeRepository.findById(id);
    }

    public Store createStore(Store store) {
        return storeRepository.save(store);
    }

    public void deleteStore(Long id) {
        storeRepository.deleteById(id);
    }

    public List<Product> getProductsForStore(Long storeId) {
    Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new RuntimeException("Store not found"));

    List<PriceHistory> priceHistories = priceHistoryRepository.findByStore(store);

    // Extragem doar produsele distinct
    return priceHistories.stream()
            .map(PriceHistory::getProduct)
            .distinct()
            .toList(); 
}

}
