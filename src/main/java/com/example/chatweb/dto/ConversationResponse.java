package com.example.chatweb.dto;

import java.util.UUID;

public record ConversationResponse(UUID id ,String nameUserA, String nameUserB) {
}
