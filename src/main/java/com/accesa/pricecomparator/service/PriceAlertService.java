package com.accesa.pricecomparator.service;

import com.accesa.pricecomparator.model.*;
import com.accesa.pricecomparator.repository.*;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceAlertService {

    private final PriceAlertRepository priceAlertRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PriceAlertNotificationService notificationService;



    public void createPriceAlert(Long userId, Long productId, double targetPrice) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));

    // dezactveaza toate alertele active anterioare pentru acelasi produs și user
    List<PriceAlert> previousAlerts = priceAlertRepository.findByUserAndProductAndActiveTrue(user, product);
    for (PriceAlert previous : previousAlerts) {
        previous.setActive(false);
    }
    priceAlertRepository.saveAll(previousAlerts);

    // creeaza o alerta noua
    PriceAlert alert = new PriceAlert(null, user, product, targetPrice, true);
    priceAlertRepository.save(alert);
}


// aceasta functie verifica daca un nou pret importat pt un produs declanseaza o alerta de pret activa,
// iar daca da, trimite notificare userului si dezactiveaza acea alert
   public void handleImportedPrice(Product product, double newPrice) {
    
    //obtinem toate alertele active pt acel produs
    List<PriceAlert> alerts = priceAlertRepository.findByProductAndActiveTrue(product);

    // se alege cea mai recenta alerta activa 
    // daca pretul curent este mai mic sau egal cu pretul tinta setat de user, se trimite o alerta
    alerts.stream()
        .max(Comparator.comparing(PriceAlert::getId))
        .ifPresent(alert -> {
            if (newPrice <= alert.getTargetPrice()) {
                notificationService.sendPriceAlertNotification(
                    alert.getUser().getId(),
                    product.getName() + " is now " + newPrice + " RON (target: " + alert.getTargetPrice() + ")"
                );
    // dezactivam alerta pentru a nu trimite notificari duplicate si o salveaza in baza de date
                alert.setActive(false);
                priceAlertRepository.save(alert);
            }
        });
}


}
