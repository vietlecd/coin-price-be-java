package com.javaweb.service;

import java.util.List;

public interface SpotWebSocketService {

    void connectToSpotWebSocket(List<String> streams);

    void closeWebSocket();
}
