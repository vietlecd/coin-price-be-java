package com.javaweb.service.authServices;

import com.javaweb.model.mongo_entity.userData;
import com.javaweb.service.CreateToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
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

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 13);

        res.setHeader("Set-Cookie", cookie + "; SameSite=None");
    }
}
