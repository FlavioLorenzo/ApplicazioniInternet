package com.internet_application.backend.Repositories;

import com.internet_application.backend.Entities.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("SELECT n FROM NotificationEntity n " +
            "WHERE n.user.id = ?1 " +
            "AND n.viewed = false " +
            "ORDER BY n.date ASC")
    List<NotificationEntity> getNewNotificationForUser(Long userId);
}
