package com.javaweb.controller.vip2;

import com.javaweb.service.trigger.CRUD.DelistingTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vip2")
public class DeleteDelistingController {

    @Autowired
    private DelistingTriggerService delistingTriggerService;

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTriggerCondition(@RequestParam("triggerType") String triggerType) {
        if ("delisting".equals(triggerType)) {
            // Gọi service để tắt thông báo cho delisting
            delistingTriggerService.disableNotifications(); // tắt tất cả thông báo
            return ResponseEntity.ok("All notifications for new delistings are now disabled.");
        } else {
            return ResponseEntity.badRequest().body("Invalid trigger type");
        }
    }
}
