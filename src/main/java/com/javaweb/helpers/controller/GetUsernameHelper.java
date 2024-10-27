//package com.javaweb.helpers.controller;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ResponseStatusException;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Component
//public class GetUsernameHelper {
//    public String getUsername(HttpServletRequest request) {
//        String username = (String) request.getAttribute("username");
//        if (username == null || username.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
//        }
//        return username;
//    }
//}
