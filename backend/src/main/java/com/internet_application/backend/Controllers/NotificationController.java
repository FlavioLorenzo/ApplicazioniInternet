package com.internet_application.backend.Controllers;

import com.internet_application.backend.Entities.NotificationEntity;
import com.internet_application.backend.PostBodies.NotificationPostBody;
import com.internet_application.backend.Services.NotificationService;
import com.internet_application.backend.Services.PrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

/*
* SECURITY POLICY
* System admin -> all access
* Others -> can only read/modify their own notifications
* */

@CrossOrigin()
@RestController
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private PrincipalService principalService;

    @GetMapping("/notifications/{notificationId}")
    public NotificationEntity getNotificationEntityWithId(
            Principal principal,
            @PathVariable(value="notificationId") Long notificationId
    )
        throws ResponseStatusException {
        /* Security check */
        NotificationEntity notification = notificationService.getNotificationEntityWithId(notificationId);
        if (!principalService.IsUserSystemAdmin(principal) &&
            !principalService.doesUserMatchPrincipal(principal, notification.getUser().getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return notification;
    }

    @GetMapping("/notifications/all/{userId}")
    public List<NotificationEntity> getNotificationsForUserWithId(
            Principal principal,
            @PathVariable(value="userId") Long userId
    )
            throws ResponseStatusException {
        /* Security check */
        if (!principalService.IsUserSystemAdmin(principal) &&
                !principalService.doesUserMatchPrincipal(principal, userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return notificationService.getNotificationsForUserWithId(userId);
    }

    @GetMapping("/notifications/pending/{userId}")
    public List<NotificationEntity> getActiveNotificationsForUserWithId(
            Principal principal,
            @PathVariable(value="userId") Long userId
    )
        throws ResponseStatusException {
        /* Security check */
        if (!principalService.IsUserSystemAdmin(principal) &&
                !principalService.doesUserMatchPrincipal(principal, userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return notificationService.getActiveNotificationsForUserWithId(userId);
    }

    @DeleteMapping("/notifications/{notificationId}")
    public void deleteNotificationWithId(
            Principal principal,
            @PathVariable(value="notificationId") Long notificationId
    )
            throws ResponseStatusException {
        /* Security check */
        NotificationEntity notification = notificationService.getNotificationEntityWithId(notificationId);
        if (!principalService.IsUserSystemAdmin(principal) &&
                !principalService.doesUserMatchPrincipal(principal, notification.getUser().getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        notificationService.deleteNotification(notificationId);
    }

    @PutMapping("/notifications/viewed/{notificationId}")
    public NotificationEntity setViewedOnNotificationWithId(
            Principal principal,
            @PathVariable(value="notificationId") Long notificationId
    )
            throws ResponseStatusException {
        /* Security check */
        NotificationEntity notification = notificationService.getNotificationEntityWithId(notificationId);
        if (!principalService.IsUserSystemAdmin(principal) &&
                !principalService.doesUserMatchPrincipal(principal, notification.getUser().getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
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
