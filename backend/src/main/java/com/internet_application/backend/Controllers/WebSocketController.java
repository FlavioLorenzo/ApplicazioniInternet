package com.internet_application.backend.Controllers;

import com.internet_application.backend.Entities.NotificationEntity;
import com.internet_application.backend.Services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WebSocketController {
    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/update/presence/{rideId}")
    @SendTo("/presence/{rideId}")
    public String updateLockedRidesFromUserAndDate(@DestinationVariable Long rideId) {
        return "Ok";
    }

    @MessageMapping("/update/notifications/{userId}")
    @SendTo("/messages/{userId}")
    public List<NotificationEntity> notificationReceived(@DestinationVariable Long userId) {
        return notificationService.getActiveNotificationsForUserWithId(userId);
    }
}
