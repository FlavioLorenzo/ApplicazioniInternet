package com.internet_application.backend.Services;

import com.internet_application.backend.Entities.NotificationEntity;

import java.util.List;

public interface NotificationService {
    NotificationEntity getNotificationEntityWithId(Long Id);
    NotificationEntity buildNotification(Long userId, String message, String url);
    NotificationEntity addNotificationEntity(NotificationEntity notificationEntity);
    void deleteNotification(Long notificationId);
    NotificationEntity setViewedNotification(Long notificationId);
    List<NotificationEntity> getActiveNotificationsForUserWithId(Long userId);
}
