package com.backend.jobbit.dto.messageDto;

import com.backend.jobbit.dto.userDto.UserDto;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConversationDto {
    private Long id;
    private UserDto user1;
    private UserDto user2;
    private LocalDateTime lastMessageTime;
    private MessageDto lastMessage;
}
