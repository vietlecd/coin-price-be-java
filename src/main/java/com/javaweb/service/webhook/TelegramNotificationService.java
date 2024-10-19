package com.javaweb.service.webhook;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class TelegramNotificationService {
    private final String BOT_TOKEN = System.getenv("TELEGRAM_BOT_TOKEN");
    private final String CHAT_ID = System.getenv("TELEGRAM_CHAT_ID");

    public void sendTriggerNotification(String message) {
        if (BOT_TOKEN == null || CHAT_ID == null) {
            System.out.println("Telegram BOT_TOKEN or CHAT_ID is not set.");
            return;
        }

        try {
            String telegramAPI = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";
            String url = telegramAPI + "?chat_id=" + CHAT_ID + "&text=" + message;
            System.out.println(url);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Message sent successfully: " + message);
            } else {
                System.out.println("Failed to send message. Response: " + response.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
