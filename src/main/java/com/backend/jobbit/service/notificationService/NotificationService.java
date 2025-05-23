package com.backend.jobbit.service.notificationService;

import com.backend.jobbit.dto.notificationsDto.NotificationDto;
import com.backend.jobbit.persistence.Notification;

import java.util.List;

public interface NotificationService {

    NotificationDto createNotification(Long userId, String type, String content, Long relatedId);

    List<NotificationDto> getUserNotifications(Long userId);

    List<NotificationDto> getUnreadNotifications(Long userId);

    int getUnreadCount(Long userId);

    void markAsRead(Long notificationId);

    void markAllAsRead(Long userId);

    void deleteNotification(Long id, Long userId);

    NotificationDto convertToDto(Notification notification);

    void clearAllNotifications(Long userId);
}
