package com.javaweb.service.webhook;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TelegramNotificationService {

    private final String APIUrl = "https://clgslsm.duckdns.org/backend";
    private final RestTemplate restTemplate = new RestTemplate();

    public void sendNotification(Map<String, Object> payload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            // Log the payload before sending the request
            System.out.println("Sending request to: " + APIUrl);
            System.out.println("Headers: " + headers);
            System.out.println("Payload: " + payload);

            // Send the request
            ResponseEntity<String> response = restTemplate.postForEntity(APIUrl, request, String.class);

            // Log the status code and response body
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response body: " + response.getBody());

            // Check if the response was successful
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Notification sent successfully: " + response.getBody());
            } else {
                System.out.println("Failed to send notification. Status code: " + response.getStatusCode());
                System.out.println("Response: " + response.getBody());
            }
        } catch (Exception e) {
            // Log the exception message
            System.out.println("Exception occurred while sending notification: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
