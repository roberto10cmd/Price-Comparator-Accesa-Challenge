package com.accesa.pricecomparator.controller;

import com.accesa.pricecomparator.dto.ProductDTO;
import com.accesa.pricecomparator.model.PriceHistory;
import com.accesa.pricecomparator.model.Product;
import com.accesa.pricecomparator.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        Product savedProduct = productService.createProduct(productDTO);
        return ResponseEntity.ok(savedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/prices")
    public ResponseEntity<List<PriceHistory>> getPriceHistoryForProduct(@PathVariable Long id) {
        List<PriceHistory> priceHistories = productService.getPriceHistoryForProduct(id);
        return ResponseEntity.ok(priceHistories);
}

}
