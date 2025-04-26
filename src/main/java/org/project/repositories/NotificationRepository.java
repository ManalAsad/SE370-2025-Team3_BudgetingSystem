package org.project.repositories;

import org.project.models.Notification;
import java.util.List;

public interface NotificationRepository extends BaseRepository<Notification, Integer> {
    List<Notification>findByUserId(int userId) throws Exception;
    List<Notification>findByUserIdAndReadFalse(int userId) throws Exception;
    Notification save(Notification notification) throws Exception;
    void markAsRead(int notificationId) throws Exception;
}