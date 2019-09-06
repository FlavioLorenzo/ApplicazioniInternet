package com.internet_application.backend.Controllers;

import com.internet_application.backend.Entities.NotificationEntity;
import com.internet_application.backend.PostBodies.NotificationPostBody;
import com.internet_application.backend.Services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin()
@RestController
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    // utente deve matchare a chi e` destinata
    @GetMapping("/notifications/{notificationId}")
    public NotificationEntity getNotificationEntityWithId(
            @PathVariable(value="notificationId") Long notificationId
    )
        throws ResponseStatusException {
        return notificationService.getNotificationEntityWithId(notificationId);
    }

    @GetMapping("/notifications/all/{userId}")
    public List<NotificationEntity> getNotificationsForUserWithId(
            @PathVariable(value="userId") Long userId
    )
            throws ResponseStatusException {
        return notificationService.getNotificationsForUserWithId(userId);
    }

    // utente deve matchare a chi e` destinata
    @GetMapping("/notifications/pending/{userId}")
    public List<NotificationEntity> getActiveNotificationsForUserWithId(
            @PathVariable(value="userId") Long userId
    )
        throws ResponseStatusException {
        return notificationService.getActiveNotificationsForUserWithId(userId);
    }

    // utente deve matchare a chi e` destinata
    @DeleteMapping("/notifications/{notificationId}")
    public void deleteNotificationWithId(
            @PathVariable(value="notificationId") Long notificationId
    )
            throws ResponseStatusException {
        notificationService.deleteNotification(notificationId);
    }

    // utente deve matchare a chi e` destinata
    @PutMapping("/notifications/viewed/{notificationId}")
    public NotificationEntity setViewedOnNotificationWithId(
            @PathVariable(value="notificationId") Long notificationId
    )
            throws ResponseStatusException {
        return notificationService.setViewedNotification(notificationId);
    }

    @PostMapping("/notifications")
    public NotificationEntity createNotificationEntity(
            @RequestBody NotificationPostBody notificationPostBody
            )
        throws ResponseStatusException {
        NotificationEntity notificationEntity = notificationService.buildNotification(
                notificationPostBody.getUserId(),
                notificationPostBody.getMessage(),
                notificationPostBody.getLink()
        );
        return notificationService.addNotificationEntity(notificationEntity);
    }

}
