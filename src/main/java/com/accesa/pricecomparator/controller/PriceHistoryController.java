package com.accesa.pricecomparator.controller;

import com.accesa.pricecomparator.dto.PriceEntryDTO;
import com.accesa.pricecomparator.dto.PriceHistoryDTO;
import com.accesa.pricecomparator.model.PriceHistory;
import com.accesa.pricecomparator.service.PriceHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/pricehistory")
@RequiredArgsConstructor
public class PriceHistoryController {

    private final PriceHistoryService priceHistoryService;

    @GetMapping
    public List<PriceHistory> getAllPriceHistories() {
        return priceHistoryService.getAllPriceHistories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriceHistory> getPriceHistoryById(@PathVariable Long id) {
        return priceHistoryService.getPriceHistoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PriceHistory> createPriceHistory(@Valid @RequestBody PriceHistoryDTO dto) {
        PriceHistory saved = priceHistoryService.createPriceHistory(dto);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePriceHistory(@PathVariable Long id) {
        priceHistoryService.deletePriceHistory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/graph")
    public ResponseEntity<List<PriceEntryDTO>> getPriceHistoryFiltered(
        @RequestParam(required = false) String productName,
        @RequestParam(required = false) String storeName,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String brand
    ) {
        return ResponseEntity.ok(
            priceHistoryService.getPriceHistoryFiltered(productName, storeName, category, brand)
        );
    }

}
