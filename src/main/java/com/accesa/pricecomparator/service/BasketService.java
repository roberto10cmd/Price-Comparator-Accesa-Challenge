package com.accesa.pricecomparator.service;
import com.accesa.pricecomparator.dto.BasketDTO;
import com.accesa.pricecomparator.model.Basket;
import com.accesa.pricecomparator.model.BasketItem;
import com.accesa.pricecomparator.model.Discount;
import com.accesa.pricecomparator.model.NutritionFacts;
import com.accesa.pricecomparator.model.PriceHistory;
import com.accesa.pricecomparator.model.Product;
import com.accesa.pricecomparator.model.User;
import com.accesa.pricecomparator.repository.BasketRepository;
import com.accesa.pricecomparator.repository.DiscountRepository;
import com.accesa.pricecomparator.repository.PriceHistoryRepository;
import com.accesa.pricecomparator.repository.UserRepository;
import com.accesa.pricecomparator.dto.BasketItemRecommendationDTO;
import com.accesa.pricecomparator.dto.NutritionStatusDTO;
import com.accesa.pricecomparator.dto.StorePriceDTO;
import com.accesa.pricecomparator.dto.BasketOptimizationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;
    private final UserRepository userRepository;
    private final PriceHistoryRepository priceHistoryRepository;
    private final DiscountRepository discountRepository;

    public List<Basket> getAllBaskets() {
        return basketRepository.findAll();
    }

    public Optional<Basket> getBasketById(Long id) {
        return basketRepository.findById(id);
    }


    /// creeaza cosul de cumparaturi pt un user existent, folosind datele din DTO
    public Basket createBasket(BasketDTO basketDTO) {
        User user = userRepository.findById(basketDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + basketDTO.getUserId()));

        Basket basket = new Basket();
        basket.setUser(user);
        basket.setName(basketDTO.getName());

        return basketRepository.save(basket);
    }

    public void deleteBasket(Long id) {
        basketRepository.deleteById(id);
    }



    // calculeaza pretul final al unui produs dupa aplicarea reducerii doar daca e valabila
    private double getDiscountedPrice(PriceHistory priceHistory) {
    Optional<Discount> optionalDiscount = discountRepository
        .findByProductAndStoreAndFromDateLessThanEqualAndToDateGreaterThanEqual(
            priceHistory.getProduct(),
            priceHistory.getStore(),
            LocalDate.now(), // aici se face comparatia cu ziua curenta
            LocalDate.now()  //
        );

    double price = priceHistory.getPrice();
    if (optionalDiscount.isPresent()) {
        double discount = optionalDiscount.get().getPercentage();
        price = price * (1 - discount / 100);
    }

    return price;
}

    private double round2Decimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }



// cauta si gaseste cel mai bun pret pt fiecare produs din basket, luand in calcul si daca sunt reduceri curente
// returneaza un dto care contine recomandari per produs si pretul total al cosului optimizat
    public BasketOptimizationDTO calculateBestPrice(Long basketId) {
    Basket basket = basketRepository.findById(basketId)
        .orElseThrow(() -> new RuntimeException("Basket not found"));

    List<BasketItemRecommendationDTO> recommendations = new ArrayList<>();
    double total = 0;

    for (BasketItem item : basket.getItems()) {
        Product product = item.getProduct();

        // cauta cea mai recenta intrare a pretului
        List<PriceHistory> latestPrices = priceHistoryRepository.findLatestValidPerStore(product);
        if (latestPrices.isEmpty()) continue;

        List<StorePriceDTO> allPrices = new ArrayList<>();

        for (PriceHistory ph : latestPrices) {
            double base = ph.getPrice();
            double discounted = getDiscountedPrice(ph);

            Optional<Discount> disc = discountRepository
                .findByProductAndStoreAndFromDateLessThanEqualAndToDateGreaterThanEqual(
                    product, ph.getStore(), LocalDate.now(), LocalDate.now());

            double pkgQty = product.getPackageQuantity();
            String unit = product.getPackageUnit().toLowerCase();

            double valuePerUnit;
            String unitLabel;


            // calculam valoare per Unitate pentru a putea compara ( optional ) preturile intre magazine
            if (unit.equals("g") || unit.equals("ml")) {
                valuePerUnit = discounted / (pkgQty / 1000.0);
                unitLabel = unit.equals("g") ? "RON/kg" : "RON/l";
            } else if (unit.equals("kg") || unit.equals("l")) {
                valuePerUnit = discounted / pkgQty;
                unitLabel = "RON/" + unit;
            } else if (unit.equals("buc")) {
                valuePerUnit = discounted / pkgQty;
                unitLabel = "RON/buc";
            } else {
                valuePerUnit = discounted / pkgQty;
                unitLabel = "RON/" + unit;
            }

            StorePriceDTO sp = new StorePriceDTO();
            sp.setStoreName(ph.getStore().getName());
            sp.setOriginalPrice(round2Decimals(base));
            sp.setDiscountedPrice(round2Decimals(discounted));
            sp.setDiscountPercentage(disc.map(Discount::getPercentage).orElse(null));
            sp.setDiscountApplied(disc.isPresent());
            sp.setValuePerUnit(round2Decimals(valuePerUnit));
            sp.setUnitLabel(unitLabel);
            sp.setDate(ph.getDate());

            allPrices.add(sp);
        }

        /// aici comparatorul a fost ales dupa pretul cel mai mic, dar se poate lua in calcul si "valuePerUnit"
        StorePriceDTO bestStorePrice = allPrices.stream()
            .min(Comparator.comparingDouble(StorePriceDTO::getDiscountedPrice))
            .orElseThrow(() -> new RuntimeException("No valid price found"));

        BasketItemRecommendationDTO dto = new BasketItemRecommendationDTO();
        dto.setProductId(product.getId());
        dto.setProductName(product.getName());
        dto.setBrand(product.getBrand());
        dto.setStoreName(bestStorePrice.getStoreName());
        dto.setOriginalPrice(bestStorePrice.getOriginalPrice());
        dto.setDiscountedPrice(bestStorePrice.getDiscountedPrice());
        dto.setDiscountPercentage(bestStorePrice.getDiscountPercentage());
        dto.setDiscountApplied(bestStorePrice.isDiscountApplied());
        dto.setAllPrices(allPrices);

        recommendations.add(dto);
        //facem totalul basketului
        total += bestStorePrice.getDiscountedPrice();
    }

    BasketOptimizationDTO result = new BasketOptimizationDTO();
    result.setBasketId(basket.getId());
    result.setRecommendations(recommendations);
    result.setTotalPrice(round2Decimals(total));

    return result;
}



// calculeaza totalul de grasimi saturate, zahar si proeteine pentru tot basketul
    public NutritionStatusDTO getNutritionStatus(Long basketId) {
        Basket basket = basketRepository.findById(basketId)
            .orElseThrow(() -> new RuntimeException("Basket not found"));

        double fat = 0.0, sugar = 0.0, protein = 0.0;

        for (BasketItem item : basket.getItems()) {
            Product product = item.getProduct();
            NutritionFacts nf = product.getNutritionFacts();
            if (nf == null) continue;

            int qty = item.getQuantity();
            double unit = product.getPackageQuantity();

            if (nf.getSaturatedFatPer100g() != null && nf.getSaturatedFatPer100g() >= 0)
                fat += (nf.getSaturatedFatPer100g() / 100.0) * unit * qty;

            if (nf.getSugarPer100g() != null && nf.getSugarPer100g() >= 0)
                sugar += (nf.getSugarPer100g() / 100.0) * unit * qty;

            if (nf.getProteinPer100g() != null && nf.getProteinPer100g() >= 0)
                protein += (nf.getProteinPer100g() / 100.0) * unit * qty;
        }

        // aceste thresholduri se pot modifica , au fost puse orientativ de mine  
        String fatStatus = fat <= 22.0
            ? "Grăsimi acceptabile (< 22g)"
            : "Prea multe grăsimi (> 22g)";

        String sugarStatus = sugar <= 30.0
            ? "Zahăr în limite normale (< 30g)"
            : "Prea mult zahăr (> 30g)";

        return new NutritionStatusDTO(
            round(fat), fatStatus,
            round(sugar), sugarStatus,
            round(protein)
        );
    }

private double round(double value) {
    return Math.round(value * 100.0) / 100.0;
}


}