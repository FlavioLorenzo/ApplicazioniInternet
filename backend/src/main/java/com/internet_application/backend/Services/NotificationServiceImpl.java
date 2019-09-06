package com.internet_application.backend.Services;

import com.internet_application.backend.Entities.NotificationEntity;
import com.internet_application.backend.Entities.UserEntity;
import com.internet_application.backend.Repositories.NotificationRepository;
import com.internet_application.backend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    // The SimpMessagingTemplate is used to send Stomp over WebSocket messages.
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public NotificationEntity getNotificationEntityWithId(Long Id)
        throws ResponseStatusException {
        NotificationEntity notificationEntity = notificationRepository.findById(Id).orElse(null);
        if (notificationEntity == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return notificationEntity;
    }

    @Override
    public NotificationEntity createNotification(Long userId, String message, String url)
            throws ResponseStatusException {
        NotificationEntity newNotification = buildNotification(userId, message, url);
        newNotification = addNotificationEntity(newNotification);

        // Send a notification to the recipient of the message
        notifyUser(userId, "new");

        return newNotification;
    }

    @Override
    public NotificationEntity buildNotification(Long userId, String message, String url)
        throws ResponseStatusException {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setUser(user);
        notificationEntity.setMessage(message);
        notificationEntity.setLink(url);
        notificationEntity.setDate(new Date());
        notificationEntity.setViewed(false);
        return notificationEntity;
    }

    @Override
    public NotificationEntity addNotificationEntity(NotificationEntity notificationEntity)
        throws ResponseStatusException {
        /*UserEntity user = notificationEntity.getUser();
        user.addNotification(notificationEntity);
        userRepository.save(user);*/
        notificationRepository.save(notificationEntity);
        return notificationEntity;
    }

    @Override
    public void deleteNotification(Long notificationId)
        throws ResponseStatusException {
        NotificationEntity notificationEntity = notificationRepository.findById(notificationId)
                .orElse(null);
        if (notificationEntity == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        /*UserEntity user = notificationEntity.getUser();
        user.removeNotification(notificationEntity);
        userRepository.save(user);*/

        notifyUser(notificationEntity.getUser().getId(), "deleted");

        notificationRepository.delete(notificationEntity);
    }

    @Override
    public NotificationEntity setViewedNotification(Long notificationId)
            throws ResponseStatusException {
        NotificationEntity notificationEntity = notificationRepository.findById(notificationId)
                .orElse(null);
        if (notificationEntity == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        notificationEntity.setViewed(true);
        notificationRepository.save(notificationEntity);

        notifyUser(notificationEntity.getUser().getId(), "viewed");
        return notificationEntity;
    }

    @Override
    public List<NotificationEntity> getNotificationsForUserWithId(Long userId)
            throws ResponseStatusException {
        return notificationRepository.getNotificationsForUser(userId);
    }

    @Override
    public List<NotificationEntity> getActiveNotificationsForUserWithId(Long userId)
        throws ResponseStatusException {
        return notificationRepository.getNewNotificationsForUser(userId);
    }

    /**
     * Send notification to users subscribed on channel "/messages/{userId}".
     *
     * The notification will be sent only to the user with the given userId.
     *
     * @param userId The id of the receiver.
     */
    public void notifyUser(Long userId, String message) {
        messagingTemplate.convertAndSend("/messages/" + userId, message);

        return;
    }
}
