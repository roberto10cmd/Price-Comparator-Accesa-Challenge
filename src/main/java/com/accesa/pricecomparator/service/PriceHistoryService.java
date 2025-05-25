package com.accesa.pricecomparator.service;

import com.accesa.pricecomparator.dto.PriceEntryDTO;
import com.accesa.pricecomparator.dto.PriceHistoryDTO;
import com.accesa.pricecomparator.model.PriceHistory;
import com.accesa.pricecomparator.model.Product;
import com.accesa.pricecomparator.model.Store;
import com.accesa.pricecomparator.repository.PriceHistoryRepository;
import com.accesa.pricecomparator.repository.ProductRepository;
import com.accesa.pricecomparator.repository.StoreRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceHistoryService {

    private final PriceHistoryRepository priceHistoryRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    public List<PriceHistory> getAllPriceHistories() {
        return priceHistoryRepository.findAll();
    }

    public Optional<PriceHistory> getPriceHistoryById(Long id) {
        return priceHistoryRepository.findById(id);
    }

    public PriceHistory createPriceHistory(PriceHistoryDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + dto.getProductId()));

        Store store = storeRepository.findById(dto.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + dto.getStoreId()));

        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setProduct(product);
        priceHistory.setStore(store);
        priceHistory.setPrice(dto.getPrice());
        priceHistory.setDate(dto.getDate());

        return priceHistoryRepository.save(priceHistory);
    }

    public void deletePriceHistory(Long id) {
        priceHistoryRepository.deleteById(id);
    }

    
    // metoda care filtreaza dupa cei 4 parametrii pentru un viitor grafic / dashboard
    public List<PriceEntryDTO> getPriceHistoryFiltered(String productName, String storeName, String category, String brand) {
    Specification<PriceHistory> spec = Specification.where(null);

    if (productName != null && !productName.isBlank()) {
        spec = spec.and((root, query, cb) -> cb.like(
            cb.lower(root.get("product").get("name")), "%" + productName.toLowerCase() + "%"
        ));
    }

    if (storeName != null && !storeName.isBlank()) {
        spec = spec.and((root, query, cb) -> cb.like(
            cb.lower(root.get("store").get("name")), "%" + storeName.toLowerCase() + "%"
        ));
    }

    if (category != null && !category.isBlank()) {
        spec = spec.and((root, query, cb) -> cb.like(
            cb.lower(root.get("product").get("category")), "%" + category.toLowerCase() + "%"
        ));
    }

    if (brand != null && !brand.isBlank()) {
        spec = spec.and((root, query, cb) -> cb.like(
            cb.lower(root.get("product").get("brand")), "%" + brand.toLowerCase() + "%"
        ));
    }

    List<PriceHistory> history = priceHistoryRepository.findAll(spec, Sort.by("date").ascending());

        return history.stream()
        .map(ph -> new PriceEntryDTO(
        ph.getDate(),
        ph.getPrice(),
        ph.getStore().getName(),
        ph.getProduct().getName(),
        ph.getProduct().getBrand()
    ))
    .toList();
}

}
