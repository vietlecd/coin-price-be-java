package com.javaweb.service;

import java.util.List;

public interface IKlineService {

    void connectToWebSocket(List<String> streams);

    void closeWebSocket();
}
