package com.example.chatweb.Service;

import com.example.chatweb.entity.Conversation;
import com.example.chatweb.entity.User;
import com.example.chatweb.repositories.ConversationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConversationService {

    private final ConversationRepository repository;

    Conversation createConversation(User userA, User userB){
        repository.findByUserAAndUserB(userA, userB)
                .ifPresent(c -> { throw new RuntimeException("essa conversa ja existe"); });

        repository.findByUserAAndUserB(userB, userA)
                .ifPresent(c -> { throw new RuntimeException("essa conversa ja existe"); });
       Conversation newChat = new Conversation(UUID.randomUUID(),userA,userB, LocalDateTime.now());
       return repository.save(newChat);
    }
    Conversation findByUsers(User userA, User userB){
        return repository.findByUserAAndUserB(userA, userB)
                .orElseGet(() -> repository.findByUserAAndUserB(userB, userA)
                        .orElseThrow(() -> new RuntimeException("conversa nao encontrada")));
    }

}
