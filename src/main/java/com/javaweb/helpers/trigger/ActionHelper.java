//package com.javaweb.helpers.trigger;
//import com.javaweb.model.trigger.TriggerCondition;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ActionHelper {
//    public void executeAction(String action, TriggerCondition triggerCondition) {
//        switch (action) {
//            case "send_sse_notification":
//                sendSseNotification(triggerCondition);
//                break;
//            case "trigger_webhook":
//                triggerWebhook(triggerCondition);
//                break;
//            default:
//                throw new IllegalArgumentException("Invalid action: " + action);
//        }
//    }
//
//    private void sendSseNotification(TriggerCondition triggerCondition) {
//        System.out.println("Sending SSE Notification for " + triggerCondition.getSymbol());
//    }
//
//    private void triggerWebhook(TriggerCondition triggerCondition) {
//        System.out.println("Triggering Webhook for " + triggerCondition.getSymbol());
//    }
//}
