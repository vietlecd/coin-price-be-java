package com.javaweb.Interceptor;

import com.javaweb.model.LoginRequest;
import com.javaweb.model.mongo_entity.userData;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.CreateToken;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Clock;

@Component
public class AuthenInterceptor implements HandlerInterceptor {
    @Autowired
    UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String token = getCookieValue(request, "token");

        if(token == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return false;
        }

        LoginRequest temp_data = CreateToken.decodeToken(token);

        if(temp_data.getPassword().equals("expired")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Phiên đăng nhập đã hết hạn");
            return false;
        }

        userData user = userRepository.findByUsername(temp_data.getUsername());

        if(user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Tài khoản hoặc mật khẩu không tồn tại");
            return false;
        }

        if(user.getPassword().equals(temp_data.getPassword())) {
            request.setAttribute("username", user.getUsername());

            return true;
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Tài khoản hoặc mật khẩu không tồn tại");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            modelAndView.addObject("extraInfo", "Additional data added in postHandle");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {

    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
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
