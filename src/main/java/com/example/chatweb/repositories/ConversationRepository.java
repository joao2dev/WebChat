package com.example.chatweb.repositories;

import com.example.chatweb.entity.Conversation;
import com.example.chatweb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {
    Optional<Conversation> findByUserAAndUserB(User userA, User userB);
}
