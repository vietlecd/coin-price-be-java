package com.javaweb.Interceptor;

import com.javaweb.model.LoginRequest;
import com.javaweb.model.mongo_entity.userData;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.CreateToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    @Autowired
    UserRepository userRepository;

    @Value("${adminToken}")
    private String adminToken;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = request.getHeader("token");
        if(token == null)
            throw new Exception("Yêu cầu xác thực quyền admin vào gọi api từ endpoint này");

        if(token.equals(adminToken)) return true;
        else throw new Exception("Sai token");
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
}
