package com.example.chatweb.controller;

import com.example.chatweb.Service.ConversationService;
import com.example.chatweb.Service.UserService;
import com.example.chatweb.dto.ConversationRequest;
import com.example.chatweb.dto.ConversationResponse;
import com.example.chatweb.entity.Conversation;
import com.example.chatweb.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("conversation")
@RequiredArgsConstructor
public class ConversationController {
    private final ConversationService service;
    private final UserService userService;

    @PostMapping("/iniciar")
    public ResponseEntity<?> startConversation(@AuthenticationPrincipal User userA , @RequestBody ConversationRequest request) {
        User userB = userService.findByUsername(request.nameUserB());
        service.createConversation(userA, userB);
        return ResponseEntity.ok("conversa iniciada");
    }
    @GetMapping("/encontrar/{id}")
    public ResponseEntity<?> findConversation(@AuthenticationPrincipal User userA , @PathVariable UUID id){
        User userB = userService.findById(id);
        Conversation conversation = service.findByUsers(userA,userB);
        return ResponseEntity.ok(new ConversationResponse(conversation.getId(),conversation.getUserA().getUsername(),conversation.getUserB().getUsername()));
    }
}
