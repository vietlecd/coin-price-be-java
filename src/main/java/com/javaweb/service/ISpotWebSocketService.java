package com.javaweb.service;

import java.util.List;

public interface ISpotWebSocketService {

    void connectToSpotWebSocket(List<String> streams);

    void closeWebSocket();
}
