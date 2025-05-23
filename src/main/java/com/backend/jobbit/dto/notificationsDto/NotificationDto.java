package com.backend.jobbit.dto.notificationsDto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private Long id;
    private Long userId;
    private String type;
    private String content;
    private Long relatedId;
    private boolean isRead;
    private LocalDateTime createdAt;
}
