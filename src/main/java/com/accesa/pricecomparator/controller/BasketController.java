package com.accesa.pricecomparator.controller;

import com.accesa.pricecomparator.dto.BasketDTO;
import com.accesa.pricecomparator.dto.BasketItemDTO;
import com.accesa.pricecomparator.dto.BasketOptimizationDTO;
import com.accesa.pricecomparator.dto.NutritionStatusDTO;
import com.accesa.pricecomparator.model.Basket;
import com.accesa.pricecomparator.model.BasketItem;
import com.accesa.pricecomparator.service.BasketItemService;
import com.accesa.pricecomparator.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/baskets")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;
    private final BasketItemService basketItemService;


    @GetMapping
    public List<Basket> getAllBaskets() {
        return basketService.getAllBaskets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Basket> getBasketById(@PathVariable Long id) {
        return basketService.getBasketById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Basket> createBasket(@Valid @RequestBody BasketDTO basketDTO) {
        Basket savedBasket = basketService.createBasket(basketDTO);
        return ResponseEntity.ok(savedBasket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBasket(@PathVariable Long id) {
        basketService.deleteBasket(id);
        return ResponseEntity.noContent().build();
    }


   @PostMapping("/{basketId}/items")
    public ResponseEntity<BasketItem> addItemToBasket(
        @PathVariable Long basketId,
        @Valid @RequestBody BasketItemDTO dto) {

        BasketItem item = basketItemService.addItemToBasket(dto, basketId);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/{basketId}/items/{itemId}")
    public ResponseEntity<Void> deleteBasketItem(
            @PathVariable Long basketId,
            @PathVariable Long itemId) {
        
        basketItemService.deleteBasketItem(itemId);
        return ResponseEntity.noContent().build();
    }

     @GetMapping("/{basketId}/optimize")
    public ResponseEntity<BasketOptimizationDTO> optimizeBasket(
            @PathVariable Long basketId) {
        BasketOptimizationDTO optimization =
                basketService.calculateBestPrice(basketId);
        return ResponseEntity.ok(optimization);
    }


    @GetMapping("/{basketId}/nutrition/status")
public ResponseEntity<NutritionStatusDTO> getNutritionStatus(@PathVariable Long basketId) {
    return ResponseEntity.ok(basketService.getNutritionStatus(basketId));
}

}
