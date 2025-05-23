package com.backend.jobbit.service.notificationService;

import com.backend.jobbit.dto.notificationsDto.NotificationDto;
import com.backend.jobbit.exception.ResourceNotFoundException;
import com.backend.jobbit.persistence.Notification;
import com.backend.jobbit.persistence.model.User;
import com.backend.jobbit.repository.NotificationRepo;
import com.backend.jobbit.repository.UserRepo;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{

    private final UserRepo userRepo;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepo notificationRepo;

    @Override
    public NotificationDto createNotification(Long userId, String type, String content, Long relatedId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setContent(content);
        notification.setRelatedId(relatedId);

        Notification saved = notificationRepo.save(notification);
        NotificationDto dto = convertToDto(saved);

        // Send real-time notification via WebSocket
        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/notifications",
                dto
        );

        return dto;
    }

    @Override
    public List<NotificationDto> getUserNotifications(Long userId) {
        return notificationRepo.findByUserUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDto> getUnreadNotifications(Long userId) {
        return notificationRepo.findByUserUserIdAndIsReadFalseOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public int getUnreadCount(Long userId) {
        return notificationRepo.countUnreadNotifications(userId);
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        notification.setRead(true);
        notificationRepo.save(notification);
    }

    @Override
    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications =
                notificationRepo.findByUserUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
        unreadNotifications.forEach(n -> n.setRead(true));
        notificationRepo.saveAll(unreadNotifications);

    }

    @Override
    public void deleteNotification(Long id, Long userId) {
        Notification notification = notificationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        if (!notification.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Cannot delete another user's notification");
        }
        notificationRepo.delete(notification);
    }

    @Override
    public NotificationDto convertToDto(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getUser().getUserId(),
                notification.getType(),
                notification.getContent(),
                notification.getRelatedId(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }

    @Override
    public void clearAllNotifications(Long userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Notification> userNotifications = notificationRepo.findByUserUserIdOrderByCreatedAtDesc(userId);
        notificationRepo.deleteAll(userNotifications);

    }
}
