package com.accesa.pricecomparator.service;

import com.accesa.pricecomparator.dto.ProductDTO;
import com.accesa.pricecomparator.model.PriceHistory;
import com.accesa.pricecomparator.model.Product;
import com.accesa.pricecomparator.repository.ProductRepository;
import com.accesa.pricecomparator.repository.PriceHistoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final PriceHistoryRepository priceHistoryRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setExternalProductId(productDTO.getExternalProductId());
        product.setName(productDTO.getName());
        product.setCategory(productDTO.getCategory());
        product.setBrand(productDTO.getBrand());
        product.setPackageQuantity(productDTO.getPackageQuantity());
        product.setPackageUnit(productDTO.getPackageUnit());
        product.setCurrency(productDTO.getCurrency());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }


    // metoda returneaza toate preturile inregistrate pt un anumit produs
    public List<PriceHistory> getPriceHistoryForProduct(Long productId) {
    Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        return priceHistoryRepository.findByProduct(product);
}

}
