package com.javaweb.controller;

import com.javaweb.model.trigger.UserTradingPair;
import com.javaweb.repository.UserTradingPairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/user-trading-pairs")
public class UserTradingPairController {

    @Autowired
    private UserTradingPairRepository userTradingPairRepository;

    @PostMapping("/{userId}")
    public void setUserTradingPairs(@PathVariable String userId, @RequestBody List<String> tradingPairs) {
        UserTradingPair userTradingPair = userTradingPairRepository.findByUserId(userId);
        if (userTradingPair == null) {
            userTradingPair = new UserTradingPair();
            userTradingPair.setUserId(userId);
        }
        userTradingPair.setTradingPairs(tradingPairs);
        userTradingPairRepository.save(userTradingPair);
    }

    @GetMapping("/{userId}")
    public List<String> getUserTradingPairs(@PathVariable String userId) {
        UserTradingPair userTradingPair = userTradingPairRepository.findByUserId(userId);
        if (userTradingPair != null) {
            return userTradingPair.getTradingPairs();
        }
        return Collections.emptyList();  // Trả về danh sách rỗng nếu không tìm thấy
    }
}
