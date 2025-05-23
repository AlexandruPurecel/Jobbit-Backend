package com.backend.jobbit.service;

import com.backend.jobbit.dto.messageDto.ConversationDto;
import com.backend.jobbit.dto.messageDto.MessageDto;
import com.backend.jobbit.dto.userDto.UserDto;
import com.backend.jobbit.persistence.Conversation;
import com.backend.jobbit.persistence.Message;
import com.backend.jobbit.persistence.model.User;
import com.backend.jobbit.repository.*;
import com.backend.jobbit.service.notificationService.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepo userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    @Transactional
    public MessageDto sendMessage(Long senderId, Long recipientId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("Sender not found"));

        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new EntityNotFoundException("Recipient not found"));

        Conversation conversation = conversationRepository
                .findByUser1_UserIdAndUser2_UserIdOrUser1_UserIdAndUser2_UserId(senderId, recipientId, recipientId, senderId)
                .orElseGet(() -> {
                    Conversation newConversation = new Conversation();
                    newConversation.setUser1(sender);
                    newConversation.setUser2(recipient);
                    newConversation.setLastMessageTime(LocalDateTime.now());
                    return conversationRepository.save(newConversation);
                });

        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setConversation(conversation);

        conversation.setLastMessageTime(message.getTimestamp());
        conversationRepository.save(conversation);

        Message savedMessage = messageRepository.save(message);

        String notificationContent = String.format("New message from %s %s",
                sender.getFirstName(), sender.getLastName());

        notificationService.createNotification(
                recipientId,                    // Who gets the notification
                "MESSAGE",                      // Type of notification
                notificationContent,            // What the notification says
                savedMessage.getId()            // Link to this message
        );


        MessageDto messageDto = convertToDto(savedMessage);
        messagingTemplate.convertAndSendToUser(
                recipient.getUserId().toString(),
                "/queue/messages",
                messageDto
        );

        return messageDto;
    }

    public List<MessageDto> getConversationMessages(Long conversationId) {
        return messageRepository.findByConversationIdOrderByTimestampAsc(conversationId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ConversationDto> getUserConversations(Long userId) {
        return conversationRepository.findByUser1_UserIdOrUser2_UserIdOrderByLastMessageTimeDesc(userId, userId)
                .stream()
                .map(this::convertConversationToDto)
                .collect(Collectors.toList());
    }

    public void markAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException("Message not found"));
        message.setRead(true);
        messageRepository.save(message);
    }

    public int getUnreadCount(Long userId) {
        return messageRepository.countByRecipientUserIdAndIsReadFalse(userId);
    }

    private MessageDto convertToDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setSenderId(message.getSender().getUserId());
        dto.setSenderName(message.getSender().getFirstName() + " " + message.getSender().getLastName());
        dto.setRecipientId(message.getRecipient().getUserId());
        dto.setRecipientName(message.getRecipient().getFirstName() + " " + message.getRecipient().getLastName());
        dto.setContent(message.getContent());
        dto.setTimestamp(message.getTimestamp());
        dto.setRead(message.isRead());
        dto.setConversationId(message.getConversation().getId());
        return dto;
    }

    private ConversationDto convertConversationToDto(Conversation conversation) {
        ConversationDto dto = new ConversationDto();
        dto.setId(conversation.getId());

        UserDto user1Dto = new UserDto();
        user1Dto.setUserId(conversation.getUser1().getUserId());
        user1Dto.setFirstName(conversation.getUser1().getFirstName());
        user1Dto.setLastName(conversation.getUser1().getLastName());
        user1Dto.setEmail(conversation.getUser1().getEmail());
        if (conversation.getUser1().getProfileImage() != null) {
            user1Dto.setProfilePictureId(conversation.getUser1().getProfileImage().getId());
        }

        UserDto user2Dto = new UserDto();
        user2Dto.setUserId(conversation.getUser2().getUserId());
        user2Dto.setFirstName(conversation.getUser2().getFirstName());
        user2Dto.setLastName(conversation.getUser2().getLastName());
        user2Dto.setEmail(conversation.getUser2().getEmail());
        if (conversation.getUser2().getProfileImage() != null) {
            user2Dto.setProfilePictureId(conversation.getUser2().getProfileImage().getId());
        }

        dto.setUser1(user1Dto);
        dto.setUser2(user2Dto);
        dto.setLastMessageTime(conversation.getLastMessageTime());


        if (!conversation.getMessages().isEmpty()) {
            Message lastMessage = conversation.getMessages().stream()
                    .max(Comparator.comparing(Message::getTimestamp))
                    .orElse(null);

            dto.setLastMessage(convertToDto(lastMessage));
        }

        return dto;
    }
}
