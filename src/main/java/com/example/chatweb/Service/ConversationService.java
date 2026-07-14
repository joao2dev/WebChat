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

    public Conversation createConversation(User userA, User userB) {
        Optional<Conversation> existente = repository.findByUserAAndUserB(userA, userB);
        if (existente.isPresent()) return existente.get();

        Optional<Conversation> existenteInverso = repository.findByUserAAndUserB(userB, userA);
        if (existenteInverso.isPresent()) return existenteInverso.get();

        Conversation newChat = new Conversation(null, userA, userB, null);
        return repository.save(newChat);
    }
    public Conversation findByUsers(User userA, User userB){
        return repository.findByUserAAndUserB(userA, userB)
                .orElseGet(() -> repository.findByUserAAndUserB(userB, userA)
                        .orElseThrow(() -> new RuntimeException("conversa nao encontrada")));
    }

}
