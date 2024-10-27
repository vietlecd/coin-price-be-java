//package com.javaweb.Interceptor;
//
//import com.javaweb.model.mongo_entity.userData;
//import com.javaweb.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestAttribute;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Controller
//public class Vip1Interceptor extends HandlerInterceptorAdapter {
//    @Autowired
//    UserRepository userRepository;
//
//    @Override
//    public boolean preHandle(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            Object handler
//            ) throws Exception {
//
//        String username = request.getAttribute("username").toString();
//
//        userData user = userRepository.findByUsername(username);
//
//        if(user.getVip_role() < 1) {
//            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Vip role không đáp ứng yêu cầu truy cập api!");
//            return false;
//        }
//
//        return true;
//    }
//}
