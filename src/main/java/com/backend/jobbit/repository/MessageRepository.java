package com.backend.jobbit.repository;

import com.backend.jobbit.persistence.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationIdOrderByTimestampAsc(Long conversationId);

    int countByRecipientUserIdAndIsReadFalse(Long recipientId);
}
