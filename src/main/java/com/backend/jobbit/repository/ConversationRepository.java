package com.backend.jobbit.repository;

import com.backend.jobbit.persistence.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByUser1_UserIdAndUser2_UserIdOrUser1_UserIdAndUser2_UserId(
            Long user1Id, Long user2Id, Long user2Id2, Long user1Id2);

    List<Conversation> findByUser1_UserIdOrUser2_UserIdOrderByLastMessageTimeDesc(
            Long userId, Long userId2);
}