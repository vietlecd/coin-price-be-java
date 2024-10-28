//package com.javaweb.service.webhook;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//
//@Service
//public class TelegramNotificationService {
//    private final String BOT_TOKEN = System.getenv("TELEGRAM_BOT_TOKEN");
//    private final String CHAT_ID = System.getenv("TELEGRAM_CHAT_ID");
//
//    private final int MAX_RETRIES = 5;
//    private final int RETRY_DELAY_MS = 1000; // Delay 1 giây giữa các lần retry
//
//    public void sendTriggerNotification(String message) {
//        if (BOT_TOKEN == null || CHAT_ID == null) {
//            System.out.println("Telegram BOT_TOKEN or CHAT_ID is not set.");
//            return;
//        }
//
//        String telegramAPI = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";
//        String url = telegramAPI + "?chat_id=" + CHAT_ID + "&text=" + message;
//        System.out.println(url);
//        RestTemplate restTemplate = new RestTemplate();
//
//        int retries = 0;
//        while (retries < MAX_RETRIES) {
//            try {
//                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
//
//                if (response.getStatusCode().is2xxSuccessful()) {
//                    System.out.println("Message sent successfully: " + message + " with: " + url);
//                    break;
//                } else {
//                    System.out.println("Failed to send message. Response: " + response.getBody());
//                    break;
//                }
//            } catch (Exception e) {
//                if (e.getMessage().contains("429")) {
//                    retries++;
//
//                    try {
//                        Thread.sleep((long) RETRY_DELAY_MS * retries);
//                    } catch (InterruptedException ie) {
//                        Thread.currentThread().interrupt();
//                    }
//                } else {
//                    e.printStackTrace();
//                    break;
//                }
//            }
//        }
//    }
//}