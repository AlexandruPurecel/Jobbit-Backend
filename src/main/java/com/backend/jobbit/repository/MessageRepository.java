package com.backend.jobbit.repository;

import com.backend.jobbit.persistence.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationIdOrderByTimestampAsc(Long conversationId);

    // Changed to use the correct property paths
    List<Message> findBySenderUserIdAndRecipientUserIdOrderByTimestampDesc(Long senderId, Long recipientId, Pageable pageable);

    // Changed to use isRead instead of read and correct path to userId
    List<Message> findByRecipientUserIdAndIsReadFalse(Long recipientId);

    // Changed to use isRead instead of read and correct path to userId
    int countByRecipientUserIdAndIsReadFalse(Long recipientId);
}
