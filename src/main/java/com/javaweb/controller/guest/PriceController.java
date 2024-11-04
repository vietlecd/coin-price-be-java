package com.javaweb.controller.guest;

import com.javaweb.connect.impl.FutureWebSocketService;
import com.javaweb.connect.impl.SpotWebSocketService;
import com.javaweb.dto.PriceDTO;
import com.javaweb.service.stream.FundingRateStreamService;
import com.javaweb.model.MarketCapResponse;
import com.javaweb.service.impl.FuturePriceDataService;
import com.javaweb.service.impl.MarketCapService;
import com.javaweb.service.impl.SpotPriceDataService;
import com.javaweb.service.stream.PriceStreamService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static com.javaweb.helpers.controller.SymbolValidate.validateSymbolsExist;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class PriceController {
    private FundingRateStreamService fundingRateStreamService;
    private PriceStreamService priceStreamService;
    private SpotPriceDataService spotPriceDataService;
    private FuturePriceDataService futurePriceDataService;
    private MarketCapService marketCapService;
    private SpotWebSocketService spotWebSocketService;
    private FutureWebSocketService futureWebSocketService;

    @GetMapping("/get-spot-price")
    public SseEmitter streamSpotPrices(@RequestParam List<String> symbols) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        spotWebSocketService.connectToWebSocket(symbols, false);

        Map<String, PriceDTO> priceDataMap = spotPriceDataService.getPriceDataUsers();
        validateSymbolsExist(symbols, priceDataMap, "Spot");

        for (String symbol : symbols) {
            priceStreamService.createPriceSseEmitter(emitter, "Spot", symbol, priceDataMap);
        }

        return emitter;
    }

    @GetMapping("/get-future-price")
    public SseEmitter streamFuturePrices(@RequestParam List<String> symbols) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        futureWebSocketService.connectToWebSocket(symbols, false);

        Map<String, PriceDTO> priceDataMap = futurePriceDataService.getPriceDataUsers();
        validateSymbolsExist(symbols, priceDataMap, "Future");

        for (String symbol : symbols) {
            priceStreamService.createPriceSseEmitter(emitter, "Future", symbol, priceDataMap);
        }

        return emitter;
    }

    @GetMapping("/get-funding-rate")
    public SseEmitter streamFundingRate(@RequestParam List<String> symbols) {
        return fundingRateStreamService.handleStreamFundingRate(symbols);
    }

    @GetMapping("/get-marketcap")
    // Phương thức mới nhận 'symbol' từ query parameter
    public List<MarketCapResponse> getMarketCap(@RequestParam List<String> symbols) {
        List<MarketCapResponse> marketCapResponses = new ArrayList<>();

        // Lặp qua từng symbol trong danh sách và lấy kết quả từ service
        for (String symbol : symbols) {
            MarketCapResponse response = marketCapService.getMarketCapBySymbol(symbol);
            if (response == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Market cap data for symbol " + symbol + " not found");
            }
            marketCapResponses.add(response);
        }

        return marketCapResponses;
    }

    @DeleteMapping("/close-all-web")
    public void closeAllWebSocket() {
        fundingRateStreamService.closeAllWebSockets();
        priceStreamService.closeAllWebSockets();
    }
}


