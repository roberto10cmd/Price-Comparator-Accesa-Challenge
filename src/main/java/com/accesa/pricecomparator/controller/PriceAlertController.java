package com.accesa.pricecomparator.controller;

import com.accesa.pricecomparator.dto.PriceAlertRequest;
import com.accesa.pricecomparator.model.PriceHistory;
import com.accesa.pricecomparator.model.Product;
import com.accesa.pricecomparator.model.Store;
import com.accesa.pricecomparator.repository.PriceHistoryRepository;
import com.accesa.pricecomparator.repository.ProductRepository;
import com.accesa.pricecomparator.repository.StoreRepository;
import com.accesa.pricecomparator.service.PriceAlertService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alerts")
public class PriceAlertController {

    private final PriceAlertService priceAlertService;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final PriceHistoryRepository priceHistoryRepository;

    
   @PostMapping
    public ResponseEntity<String> createPriceAlert(@RequestBody PriceAlertRequest request) {
        priceAlertService.createPriceAlert(
           request.getUserId(),
           request.getProductId(),
           request.getTargetPrice()
     );
    return ResponseEntity.ok("Alert created successfully.");
}

 @PostMapping("/trigger-alert")
    public ResponseEntity<String> testAlert(@RequestParam Long productId,
                                            @RequestParam double price,
                                            @RequestParam String storeName) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        Store store = storeRepository.findByNameIgnoreCase(storeName)
            .orElseThrow(() -> new RuntimeException("Store not found"));

        PriceHistory ph = new PriceHistory();
        ph.setProduct(product);
        ph.setStore(store);
        ph.setDate(LocalDate.now());
        ph.setPrice(price);

        priceHistoryRepository.save(ph);

        // 👉 declanșează notificarea dacă e cazul
        priceAlertService.handleImportedPrice(product, price);

        return ResponseEntity.ok("Test price imported and alert checked.");
    }

}
