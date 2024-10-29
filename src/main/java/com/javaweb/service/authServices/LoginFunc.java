package com.javaweb.service.authServices;

import com.javaweb.model.mongo_entity.userData;
import com.javaweb.service.CreateToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginFunc {
    public static String getClientIp(HttpServletRequest request) throws Exception {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        }
        else {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    public static void setCookie(String username, HttpServletResponse res) throws Exception {
        String token = CreateToken.createToken(username);

        ResponseCookie cookie = ResponseCookie.from("token", token)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(60*60*13)
                .build();

        String cookieHeader = cookie.toString() + "; SameSite=None";
//        Cookie cookie = new Cookie("token", token);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(false);
//        cookie.setPath("/");
//        cookie.setMaxAge(60 * 60 * 13);

        res.setHeader(HttpHeaders.SET_COOKIE, cookieHeader);
    }
}
