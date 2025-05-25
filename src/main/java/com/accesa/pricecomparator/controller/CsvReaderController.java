package com.accesa.pricecomparator.controller;

import com.accesa.pricecomparator.service.CsvReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.accesa.pricecomparator.service.CSVNutritionReaderService;
@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class CsvReaderController {

    private final CsvReaderService csvReaderService;
    private final CSVNutritionReaderService csvNutritionReaderService;
    @PostMapping("/products")
    public ResponseEntity<String> importProducts(@RequestParam("file") MultipartFile file) throws Exception {
        csvReaderService.importProductsAndPrices(file);
        return ResponseEntity.ok("Products imported successfully!");
    }

    @PostMapping("/discounts")
    public ResponseEntity<String> importDiscounts(@RequestParam("file") MultipartFile file) throws Exception {
        csvReaderService.importDiscounts(file);
        return ResponseEntity.ok("Discounts imported successfully!");
    }

    @PostMapping("/nutrition")
    public ResponseEntity<String> importNutrition(@RequestParam("file") MultipartFile file) throws Exception {
        csvNutritionReaderService.importNutritionFacts(file);
        return ResponseEntity.ok("Nutrition facts imported successfully!");
    }
}
