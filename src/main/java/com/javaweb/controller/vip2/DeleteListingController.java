package com.javaweb.controller.vip2;

import com.javaweb.service.trigger.CRUD.ListingTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/vip2")
public class DeleteListingController {

    @Autowired
    private ListingTriggerService listingTriggerService;

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTriggerCondition(@RequestParam("triggerType") String triggerType) {
        if ("listing".equals(triggerType)) {
            listingTriggerService.disableNotifications(); // Tắt tất cả thông báo cho các symbol mới
            return ResponseEntity.ok("All notifications for new listings are now disabled.");
        } else {
            return ResponseEntity.badRequest().body("Invalid trigger type");
        }
    }
}

