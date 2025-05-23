package com.backend.jobbit.repository;

import com.backend.jobbit.persistence.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification, Long> {

    List<Notification> findByUserUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);

    List<Notification> findByUserUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.userId = :userId AND n.isRead = false")
    int countUnreadNotifications(Long userId);
}
