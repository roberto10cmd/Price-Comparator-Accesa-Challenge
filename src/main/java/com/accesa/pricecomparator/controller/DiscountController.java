package com.accesa.pricecomparator.controller;

import com.accesa.pricecomparator.dto.DiscountDTO;
import com.accesa.pricecomparator.dto.DiscountProductDTO;
import com.accesa.pricecomparator.model.Discount;
import com.accesa.pricecomparator.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    @GetMapping
    public List<Discount> getAllDiscounts() {
        return discountService.getAllDiscounts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Discount> getDiscountById(@PathVariable Long id) {
        return discountService.getDiscountById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Discount> createDiscount(@Valid @RequestBody DiscountDTO dto) {
        Discount saved = discountService.createDiscount(dto);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.noContent().build();
    }

   @GetMapping("/best")
    public ResponseEntity<Map<String, List<DiscountProductDTO>>> getTop3DiscountsPerStore() {
        Map<String, List<DiscountProductDTO>> result = discountService.getTop3DiscountsPerStore();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/new")
    public ResponseEntity<List<DiscountProductDTO>> getNewDiscounts(@RequestParam int days) {
        return ResponseEntity.ok(discountService.getNewDiscounts(days));
}


}
