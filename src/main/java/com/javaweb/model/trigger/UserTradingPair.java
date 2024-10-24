package com.javaweb.model.trigger;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "userTradingPairs")
public class UserTradingPair {

    @Id
    private String userId;  // ID người dùng
    private List<String> tradingPairs;  // Danh sách cặp giao dịch người dùng chọn

    // Getter và Setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getTradingPairs() {
        return tradingPairs;
    }

    public void setTradingPairs(List<String> tradingPairs) {
        this.tradingPairs = tradingPairs;
    }
}
