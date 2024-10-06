package com.javaweb.service;

import java.util.List;

public interface FundingRateWebSocketService {
    // Connect to WebSocket for Funding Rate
    void connectToFundingRateWebSocket(List<String> streams);

    void closeWebSocket();
}
