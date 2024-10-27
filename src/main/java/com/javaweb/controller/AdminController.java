//package com.javaweb.controller;
//
//import com.javaweb.model.mongo_entity.userData;
//import com.javaweb.repository.UserRepository;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.Date;
//import java.util.List;
//
//@RestController
//@RequestMapping("/admin")
//public class AdminController {
//    @Autowired
//    UserRepository userRepository;
//
//    @DeleteMapping("/removeUserByUsername")
//    public ResponseEntity<?> getUser(@RequestParam String username) {
//        userData userData = userRepository.findByUsername(username);
//        if(userData == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
//        }
//
//        userRepository.deleteByUsername(userData.getUsername());
//
//        return new ResponseEntity<>(
//                new Responses(
//                        new Date(),
//                        "200",
//                        "Xóa user thành công!",
//                        "/admin/removeUserByUsername"),
//                HttpStatus.OK);
//    }
//
//    @GetMapping("/getAllUser")
//    public List<userData> getAllUser() {
//        List<userData> userDataList = userRepository.findAll();
//
//        return userDataList;
//    }
//
//    @Data
//    @AllArgsConstructor
//    private class Responses{
//        private Date timestamp;
//        private String status;
//        private String message;
//        private String path;
//    }
//}
