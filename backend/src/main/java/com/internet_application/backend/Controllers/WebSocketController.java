package com.internet_application.backend.Controllers;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @MessageMapping("/update/presence/{rideId}")
    @SendTo("/presence/{rideId}")
    public String updateLockedRidesFromUserAndDate(@DestinationVariable Long rideId) {
        return "Ok";
    }
}
