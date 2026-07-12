package com.example.chatweb.dto;

import java.time.LocalDateTime;

public record GroupMessageRequest(String content, String senderName, String groupName) {
}
