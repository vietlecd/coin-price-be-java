package com.javaweb.service.authServices;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class OtherFunct {
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
