package com.javaweb.controller;

import com.javaweb.DTO.FundingIntervalDTO;
import com.javaweb.DTO.FundingRateDTO;
import com.javaweb.DTO.PriceDTO;
import com.javaweb.config.WebSocketConfig;
import com.javaweb.connect.IFundingIntervalWebService;
import com.javaweb.connect.IFundingRateWebSocketService;
import com.javaweb.connect.IFutureWebSocketService;
import com.javaweb.connect.ISpotWebSocketService;
import com.javaweb.helpers.SseHelper;
import com.javaweb.service.IFundingRateDataService;
import com.javaweb.service.IFuturePriceDataService;
import com.javaweb.service.ISpotPriceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/api")
public class PriceController {

    @Autowired
    private SseHelper sseHelper;

    @Autowired
    private WebSocketConfig webSocketConfig;

    @Autowired
    private ISpotPriceDataService spotPriceDataService;
    @Autowired
    private IFuturePriceDataService futurePriceDataService;
    @Autowired
    private IFundingRateDataService fundingRateDataService;

    @Autowired
    private ISpotWebSocketService spotWebSocketService;
    @Autowired
    private IFutureWebSocketService futureWebSocketService;
    @Autowired
    private IFundingRateWebSocketService fundingRateWebSocketService;
    @Autowired
    private IFundingIntervalWebService fundingIntervalWebService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    @GetMapping("/start-websockets-sse")
    public SseEmitter startWebsocketsWithSSE(@RequestParam List<String> symbols) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);  // Duy trì kết nối vô hạn

        executorService.submit(() -> {
            try {
                // Khởi động Spot WebSocket trong một luồng khác
                executorService.submit(() -> {
                    spotWebSocketService.connectToSpotWebSocket(symbols);
                    while (true) {  // Duy trì kết nối liên tục
                        Map<String, PriceDTO> spotPriceData = spotPriceDataService.getSpotPriceDataMap();
                        try {
                            emitter.send(spotPriceData);  // Gửi dữ liệu liên tục
                            Thread.sleep(1000);  // Giả lập gửi dữ liệu liên tục mỗi 1 giây
                        } catch (IOException | InterruptedException e) {
                            emitter.completeWithError(e);
                            break;
                        }
                    }
                });

                // Khởi động Future WebSocket trong một luồng khác
                executorService.submit(() -> {
                    futureWebSocketService.connectToFutureWebSocket(symbols);
                    while (true) {  // Duy trì kết nối liên tục
                        Map<String, PriceDTO> futurePriceData = futurePriceDataService.getFuturePriceDataMap();
                        try {
                            emitter.send(futurePriceData);  // Gửi dữ liệu liên tục
                            Thread.sleep(1000);
                        } catch (IOException | InterruptedException e) {
                            emitter.completeWithError(e);
                            break;
                        }
                    }
                });

                // Khởi động Funding Rate WebSocket trong một luồng khác
                executorService.submit(() -> {
                    fundingRateWebSocketService.connectToFundingRateWebSocket(symbols);
                    while (true) {  // Duy trì kết nối liên tục
                        Map<String, FundingRateDTO> fundingRateData = fundingRateDataService.getFundingRateDataMap();
                        try {
                            emitter.send(fundingRateData);  // Gửi dữ liệu liên tục
                            Thread.sleep(1000);
                        } catch (IOException | InterruptedException e) {
                            emitter.completeWithError(e);
                            break;
                        }
                    }
                });

            } catch (Exception e) {
                emitter.completeWithError(e);  // Nếu có lỗi xảy ra, SSE gửi thông báo lỗi
            }
        });

        return emitter;  // Trả về SseEmitter để client nhận dữ liệu liên tục
    }

    @GetMapping("/get-spot-price")
    public SseEmitter streamSpotPrices(@RequestParam List<String> symbols) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        spotWebSocketService.connectToSpotWebSocket(symbols);

        Map<String, PriceDTO> priceDataMap = spotPriceDataService.getSpotPriceDataMap();
        return sseHelper.createPriceSseEmitter(emitter, "spot", priceDataMap, webSocketConfig);
    }

    @GetMapping("/get-future-price")
    public SseEmitter streamFuturePrices(@RequestParam List<String> symbols) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        futureWebSocketService.connectToFutureWebSocket(symbols);

        Map<String, PriceDTO> priceDataMap = futurePriceDataService.getFuturePriceDataMap();
        return sseHelper.createPriceSseEmitter(emitter, "future", priceDataMap, webSocketConfig);
    }

    @GetMapping("/get-funding-rate")
    public SseEmitter streamFundingRate(@RequestParam List<String> symbols) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        fundingRateWebSocketService.connectToFundingRateWebSocket(symbols);

        Map<String, FundingRateDTO> fundingRateDataMap = fundingRateDataService.getFundingRateDataMap();
        return sseHelper.createFundingRateSseEmitter(emitter, "funding-rate", fundingRateDataMap, webSocketConfig);
    }

    @GetMapping("/get-funding-interval")
    public ResponseEntity<List<Map<String, FundingIntervalDTO>>> getFundingInterval(@RequestParam List<String> symbols) {
        if (symbols == null || symbols.isEmpty()) {
            return ResponseEntity.badRequest().build();  // Trả về lỗi 400 nếu danh sách symbols rỗng
        }

        List<String> upperCaseSymbols = new ArrayList<>();
        for (String symbol : symbols) {
            upperCaseSymbols.add(symbol.toUpperCase());
        }

        // Gọi service để lấy dữ liệu
        List<Map<String, FundingIntervalDTO>> fundingIntervalData = fundingIntervalWebService.handleFundingIntervalWeb(upperCaseSymbols);

        // Trả về dữ liệu thị trường dưới dạng JSON
        return ResponseEntity.ok(fundingIntervalData);
    }

    @DeleteMapping("/close-websocket")
    public void closeWebSocket(@RequestParam String type) {
        sseHelper.closeWebSocket(type, webSocketConfig);
    }
}
