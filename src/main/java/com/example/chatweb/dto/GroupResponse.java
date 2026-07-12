package com.example.chatweb.dto;

import java.util.UUID;

public record GroupResponse(UUID id, String name , String ownerUsername) {
}
