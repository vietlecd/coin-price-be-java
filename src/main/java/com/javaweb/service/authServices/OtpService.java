package com.javaweb.service.authServices;


import com.javaweb.model.mongo_entity.userData;
import com.javaweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OtpService {
    @Autowired
    UserRepository userRepository;

//    @Scheduled(fixedRate = 60000)
//    public void deleteExpiredOtp() {
//        List<userData> listUser = userRepository.findAll();
//        for (userData user : listUser) {
//            if(user.getOtp()!=null && user.getOtp().isExpired()) {
//                user.setOtp(null);
//                userRepository.deleteByUsername(user.getUsername());
//                userRepository.save(user);
//            }
//        }
//    }
}
