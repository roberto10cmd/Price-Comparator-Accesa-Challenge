package com.accesa.pricecomparator.controller;

import com.accesa.pricecomparator.dto.StoreDTO;
import com.accesa.pricecomparator.model.Product;
import com.accesa.pricecomparator.model.Store;
import com.accesa.pricecomparator.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public List<Store> getAllStores() {
        return storeService.getAllStores();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Store> getStoreById(@PathVariable Long id) {
        return storeService.getStoreById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Store> createStore(@Valid @RequestBody StoreDTO storeDTO) {
        Store store = new Store();
        store.setName(storeDTO.getName());

        Store savedStore = storeService.createStore(store);
        return ResponseEntity.ok(savedStore);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
        storeService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<Product>> getProductsForStore(@PathVariable Long id) {
        List<Product> products = storeService.getProductsForStore(id);
        return ResponseEntity.ok(products);
}

}
