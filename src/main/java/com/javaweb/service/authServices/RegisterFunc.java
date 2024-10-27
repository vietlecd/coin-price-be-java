//package com.javaweb.service.authServices;
//
//import com.javaweb.model.RegisterRequest;
//import com.javaweb.model.mongo_entity.userData;
//import com.javaweb.repository.UserRepository;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.server.ResponseStatusException;
//
//public class RegisterFunc {
//    public static void checkUserAndEmail(userData userData, UserRepository userRepository) {
//        if(checkUser(userData.getUsername(), userRepository)) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username này đã được đăng kí!");
//        }
//
//        if(checkEmail(userData.getEmail(), userRepository)) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email này đã được đăng kí!");
//        }
//    }
//
//    public static void bodyInformationCheck(@RequestBody RegisterRequest req) {
//        if(req.getName() == null) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Chưa nhập tên");
//        }
//
//        if(req.getEmail() == null) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Chưa nhập địa chỉ Email");
//        }
//
//        if(req.getUsername() == null) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Chưa nhập Username");
//        }
//
//        if(req.getPassword() == null) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Chưa nhập Password");
//        }
//    }
//
//    public static boolean checkUser(String user, UserRepository userRepository) {
//        return userRepository.findByUsername(user) != null;
//    }
//
//    public static boolean checkEmail(String email, UserRepository userRepository) {
//        return userRepository.findByEmail(email) != null;
//    }
//}
