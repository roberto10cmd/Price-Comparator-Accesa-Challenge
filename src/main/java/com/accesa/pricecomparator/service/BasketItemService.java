package com.accesa.pricecomparator.service;

import com.accesa.pricecomparator.dto.BasketItemDTO;
import com.accesa.pricecomparator.model.Basket;
import com.accesa.pricecomparator.model.BasketItem;
import com.accesa.pricecomparator.model.Product;
import com.accesa.pricecomparator.repository.BasketItemRepository;
import com.accesa.pricecomparator.repository.BasketRepository;
import com.accesa.pricecomparator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasketItemService {

    private final BasketItemRepository basketItemRepository;
    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;

   public BasketItem addItemToBasket(BasketItemDTO dto, Long basketId) {
    Basket basket = basketRepository.findById(basketId)
            .orElseThrow(() -> new RuntimeException("Basket not found"));

    Product product = productRepository.findById(dto.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not found"));

    BasketItem item = new BasketItem();
    item.setBasket(basket);
    item.setProduct(product);
    item.setQuantity(dto.getQuantity());

    return basketItemRepository.save(item);
}


    public void deleteBasketItem(Long id) {
        basketItemRepository.deleteById(id);
    }
}
