package com.javaweb.service.webhook;

import com.javaweb.dto.telegram.TelegramNotificationDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class TelegramNotificationService {

    private final String APIUrl = "https://clgslsm-tele-bot-8e92f25bcbac.herokuapp.com/backend";
    private final RestTemplate restTemplate = new RestTemplate();
    public void sendTriggerNotification(TelegramNotificationDTO notification) {

        Map<String, Object> payload = new HashMap<>();
        payload.put("symbol", notification.getSymbol());
        payload.put("price", notification.getPrice());
        payload.put("threshold", notification.getThreshold());
        payload.put("condition", notification.getCondition());
        payload.put("vip_role", notification.getVip_role());
        payload.put("chatID", notification.getChatId());
        payload.put("timestamp", notification.getTimestamp());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String > response = restTemplate.postForEntity(APIUrl, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Notification sent successfully: " + response.getBody());
            } else {
                System.out.println("Failed to send notification. Status code: " + response.getStatusCode());
                System.out.println("Response: " + response.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}