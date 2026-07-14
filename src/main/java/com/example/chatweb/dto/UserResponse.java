package com.example.chatweb.dto;

import java.util.UUID;

public record UserResponse(UUID id, String username, String email) {
}
