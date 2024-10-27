//package com.javaweb.service.authServices;
//
//import com.javaweb.model.mongo_entity.userData;
//import com.javaweb.service.CreateToken;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.server.ResponseStatusException;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//public class LoginFunc {
//    public static String getClientIp(HttpServletRequest request) {
//        String ip = request.getHeader("x-forwarded-for");
//        if (ip != null && !ip.isEmpty()) {
//            ip = ip.split(",")[0];
//        }
//        else {
//            ip = request.getRemoteAddr();
//        }
//
//        return ip;
//    }
//    public static void checkUser(userData user) {
//        if(user == null) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai tên đăng nhập hoặc mật khẩu");
//        }
//    }
//    public static void setCookie(String username, String password, HttpServletResponse res) {
//        String token = CreateToken.createToken(username, password);
//
//        Cookie cookie = new Cookie("token", token);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(false);
//        cookie.setPath("/");
//        cookie.setMaxAge(60 * 60 * 13);
//
//        res.addCookie(cookie);
//    }
//}
