package com.example.chatweb.dto;

import com.example.chatweb.entity.Friendship;

import java.util.UUID;

public record FriendshipResponse(UUID id, String requesterUsername, String addresseeUsername, Friendship.FriendshipStatus status) {
}
