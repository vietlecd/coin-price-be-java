package com.javaweb.controller;

import com.javaweb.model.mongo_entity.paymentHistory;
import com.javaweb.model.mongo_entity.userData;
import com.javaweb.repository.PaymentRepository;
import com.javaweb.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @DeleteMapping("/removeUserByUsername")
    public ResponseEntity<?> getUser(@RequestParam String username) {
        userData userData = userRepository.findByUsername(username);
        if(userData == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        userRepository.deleteByUsername(userData.getUsername());

        return new ResponseEntity<>(
                new Responses(
                        new Date(),
                        "200",
                        "Xóa user thành công!",
                        "/admin/removeUserByUsername"),
                HttpStatus.OK);
    }

    @GetMapping("/getAllUser")
    public List<userData> getAllUser() {
        List<userData> userDataList = userRepository.findAll();

        return userDataList;
    }

    @DeleteMapping("/deleteAllUsername")
    public ResponseEntity<?> deleteAllUsername() {
        try {
            userRepository.deleteAll();

            return new ResponseEntity<>(
                    new Responses(
                            new Date(),
                            "400",
                            "Xóa toàn bộ user thành công!",
                            "/admin/deleteAllUsername"),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Responses(
                            new Date(),
                            "400",
                            e.getMessage(),
                            "/admin/deleteAllUsername"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getHistoryPayment")
    public List<paymentHistory> getHistoryPayment(
                                                  @RequestParam(required = false) Integer from,
                                                  @RequestParam(required = false) Integer to
    ) {
        try {
            List<paymentHistory> list = paymentRepository.findAll();

            if(from != null && to != null) {
                if(from > to) {
                    throw new Exception("Yêu cầu không hợp lệ!, from > to");
                }

                if(from < 0 || to > list.size()) {
                    throw new Exception("Yêu cầu không hợp lệ, from < 0 || to < 0");
                }

                return list.subList(from, to);
            }
            return list;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Data
    @AllArgsConstructor
    private class Responses{
        private Date timestamp;
        private String status;
        private String message;
        private String path;
    }
}
