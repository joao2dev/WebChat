package com.example.chatweb.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record GroupMessageResponse(UUID id, String content, String senderName, String groupName, LocalDateTime sentAt) {
}
