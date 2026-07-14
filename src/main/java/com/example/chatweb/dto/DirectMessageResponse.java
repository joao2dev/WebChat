package com.example.chatweb.dto;

import com.example.chatweb.entity.Conversation;

import java.time.LocalDateTime;
import java.util.UUID;

public record DirectMessageResponse(UUID id, String content, String senderUsername, UUID conversationId, LocalDateTime sentAt) {}
