package com.javaweb.service.trigger.CRUD;

import com.javaweb.dto.FundingIntervalDTO;
import com.javaweb.model.trigger.FundingInterval;
import com.javaweb.repository.trigger.FundingIntervalRepository;
import com.javaweb.service.webhook.TelegramNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Service
public class FundingIntervalService {

    @Autowired
    private FundingIntervalRepository fundingIntervalRepository;

    @Autowired
    private TelegramNotificationService telegramNotificationService;

    private final RestTemplate restTemplate = new RestTemplate();

    // Tạo trigger mới với symbol và username từ body
    public String createTrigger(FundingIntervalDTO fundingIntervalDTO, String username) {
        // Kiểm tra nếu symbol chưa được cung cấp trong request
        if (fundingIntervalDTO.getSymbol() == null || fundingIntervalDTO.getSymbol().isEmpty()) {
            return "{\"alert_id\": \"Symbol cannot be null or empty\", \"message\": \"Alert creation failed\"}";
        }

        // Kiểm tra nếu trigger đã tồn tại cho symbol
        FundingInterval existing = fundingIntervalRepository.findBySymbol(fundingIntervalDTO.getSymbol());
        if (existing != null) {
            return "{\"alert_id\": \"Trigger already exists for symbol: " + fundingIntervalDTO.getSymbol() + "\", \"message\": \"Alert creation failed\"}";
        }

        // Lấy thông tin funding từ API Binance cho symbol
        Map<String, Object> fundingInfo = getFundingInfoFromBinance(fundingIntervalDTO.getSymbol());
        if (fundingInfo == null) {
            return "{\"alert_id\": \"Unable to fetch funding info for symbol: " + fundingIntervalDTO.getSymbol() + "\", \"message\": \"Alert creation failed\"}";
        }

        // Trích xuất các trường cần thiết từ phản hồi
        Long fundingIntervalHours = ((Number) fundingInfo.get("fundingIntervalHours")).longValue();
        String adjustedFundingRateCap = fundingInfo.get("adjustedFundingRateCap").toString();
        String adjustedFundingRateFloor = fundingInfo.get("adjustedFundingRateFloor").toString();

        // Tạo một đối tượng FundingInterval mới với dữ liệu từ request và API
        FundingInterval fundingInterval = new FundingInterval(
                fundingIntervalDTO.getSymbol(),
                fundingIntervalHours,
                adjustedFundingRateCap,
                adjustedFundingRateFloor
        );
        fundingIntervalRepository.save(fundingInterval);

        // Kiểm tra nếu funding interval đã thay đổi và gửi thông báo nếu có
        if (isFundingIntervalChanged(fundingIntervalDTO.getSymbol(), fundingIntervalHours)) {
            sendNotification(fundingIntervalDTO.getSymbol(), fundingIntervalHours);
        }

        return "{\"alert_id\": \"Trigger created successfully for symbol: " + fundingIntervalDTO.getSymbol() + "\", \"message\": \"Alert creation successful\"}";
    }






    // Phương thức lấy thông tin funding từ Binance API
    private Map<String, Object> getFundingInfoFromBinance(String symbol) {
        String url = "https://api.binance.com/fapi/v1/fundingInfo?symbol=" + symbol;
        try {
            ResponseEntity<Map[]> response = restTemplate.getForEntity(url, Map[].class);
            if (response.getBody() != null && response.getBody().length > 0) {
                return response.getBody()[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Kiểm tra nếu funding interval thay đổi
    private boolean isFundingIntervalChanged(String symbol, Long newFundingIntervalHours) {
        FundingInterval existing = fundingIntervalRepository.findBySymbol(symbol);
        return existing != null && !existing.getFundingIntervalHours().equals(newFundingIntervalHours);
    }

    // Gửi thông báo qua Telegram
    private void sendNotification(String symbol, Long fundingIntervalHours) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("message", String.format("Funding interval for symbol %s has changed to %d hours.", symbol, fundingIntervalHours));
        telegramNotificationService.sendNotification(payload);
    }


    public void deleteTrigger(String symbol, String username) {
        FundingInterval existing = fundingIntervalRepository.findBySymbol(symbol);
        if (existing != null) {
            // Optional: Use username if you need to log or handle it before deletion
            fundingIntervalRepository.delete(existing);
        }
    }

}
