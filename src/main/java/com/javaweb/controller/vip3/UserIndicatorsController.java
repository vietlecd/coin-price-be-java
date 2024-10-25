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

@RestController
@RequestMapping("/api/vip3")
public class UserIndicatorsController {

    @Autowired
    private IUserIndicatorService userIndicatorService;

    @PostMapping("/user-indicators")
    public ResponseEntity<String> addUserIndicator(HttpServletRequest request, @RequestBody UserIndicatorRequest userIndicatorRequest) {
        String username = (String) request.getAttribute("username");
        userIndicator userIndicator = new userIndicator();
        userIndicator.setName(userIndicatorRequest.getName());
        userIndicator.setCode(userIndicatorRequest.getCode());
        userIndicator.setUsername(username);

        String code = userIndicatorService.getCode(username, userIndicatorRequest.getName());
        if (code != null) {
            return ResponseEntity.badRequest().body("Indicator " + userIndicatorRequest.getName() + " đã tồn tại!");
        }

        userIndicatorService.addIndicator(userIndicator);

        return ResponseEntity.ok("Đã thêm indicator " + userIndicatorRequest.getName() + " thành công!");
    }
}
