package com.example.chatweb.Config;

import lombok.Builder;

@Builder
public record JWTUserData(String username) {
}
