package com.accesa.pricecomparator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceAlertNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    // trimitem mesaje de alerta pe acest topic
    public void sendPriceAlertNotification(Long userId, String message) {
        messagingTemplate.convertAndSend("/topic/alerts/" + userId, message);
    }
}
