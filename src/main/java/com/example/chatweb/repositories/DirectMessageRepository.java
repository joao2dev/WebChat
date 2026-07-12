package com.example.chatweb.repositories;

import com.example.chatweb.entity.Conversation;
import com.example.chatweb.entity.DirectMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DirectMessageRepository extends JpaRepository<DirectMessage, UUID> {
    List<DirectMessage> findByConversationOrderBySentAtAsc(Conversation conversation);
}
