package com.backend.jobbit.dto.messageDto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private Long id;
    private Long senderId;
    private String senderName;
    private Long recipientId;
    private String recipientName;
    private String content;
    private LocalDateTime timestamp;
    private boolean read;
    private Long conversationId;
}