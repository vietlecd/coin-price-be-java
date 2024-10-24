package com.javaweb.service.authServices;

import com.javaweb.model.RegisterRequest;
import com.javaweb.model.mongo_entity.userData;
import com.javaweb.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

public class RegisterFunc {
    public static void checkUserAndEmail(userData userData, UserRepository userRepository) throws Exception {
        if(checkUser(userData.getUsername(), userRepository)) {
            throw new Exception("Username này đã được đăng kí!");
        }

        if(checkEmail(userData.getEmail(), userRepository)) {
            throw new Exception("Email này đã được đăng kí!");
        }
    }

    public static void bodyInformationCheck(@RequestBody RegisterRequest req) throws Exception {
        if(req.getName() == null) {
            throw new Exception("Chưa nhập tên");
        }

        if(req.getEmail() == null) {
            throw new Exception("Chưa nhập địa chỉ Email");
        }

        if(req.getUsername() == null) {
            throw new Exception("Chưa nhập Username");
        }

        if(req.getPassword() == null) {
            throw new Exception("Chưa nhập Password");
        }
    }

    public static boolean checkUser(String user, UserRepository userRepository) {
        return userRepository.findByUsername(user) != null;
    }

    public static boolean checkEmail(String email, UserRepository userRepository) {
        return userRepository.findByEmail(email) != null;
    }
}
