package com.javaweb.controller.vip3;

import com.javaweb.model.UserIndicatorRequest;
import com.javaweb.model.mongo_entity.userIndicator;
import com.javaweb.service.IUserIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/api/vip3")
public class UserIndicatorsController {

    @Autowired
    private IUserIndicatorService userIndicatorService;

    @PostMapping("/user-indicators")
    public ResponseEntity<String> addUserIndicator(HttpServletRequest request, @RequestBody UserIndicatorRequest userIndicatorRequest) {
        String username = (String) request.getAttribute("username");

        Optional<userIndicator> existingIndicator = userIndicatorService.findByUsernameAndName(username, userIndicatorRequest.getName());
        try {
            if (existingIndicator.isPresent()) {
                userIndicator userIndicator = existingIndicator.get();
                userIndicator.setCode(userIndicatorRequest.getCode());
                userIndicatorService.addIndicator(userIndicator);

                return ResponseEntity.ok("Indicator " + userIndicatorRequest.getName() + " đã được cập nhật thành công!");
            } else {
                userIndicator userIndicator = new userIndicator();
                userIndicator.setUsername(username);
                userIndicator.setName(userIndicatorRequest.getName());
                userIndicator.setCode(userIndicatorRequest.getCode());

                userIndicatorService.addIndicator(userIndicator);

                return ResponseEntity.ok("Đã thêm indicator " + userIndicatorRequest.getName() + " thành công!");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
