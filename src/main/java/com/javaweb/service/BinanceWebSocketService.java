package com.javaweb.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface BinanceWebSocketService {

    void connectToWebSocket(List<String> streams);

    void closeWebSocket();
}
