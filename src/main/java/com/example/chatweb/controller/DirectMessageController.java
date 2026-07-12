package com.example.chatweb.controller;
import com.example.chatweb.Service.ConversationService;
import com.example.chatweb.Service.DirectMessageService;
import com.example.chatweb.Service.UserService;
import com.example.chatweb.dto.DirectMenssageRequest;
import com.example.chatweb.dto.DirectMessageResponse;
import com.example.chatweb.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@RestController
@RequestMapping("direct")
@RequiredArgsConstructor
public class DirectMessageController {
    private final DirectMessageService service;
    private final ConversationService conversationService;
    private final UserService userService;

    @PostMapping("/enviar")
    public ResponseEntity<?> sendMessageInGroup(@AuthenticationPrincipal User sender , @RequestBody DirectMenssageRequest request){
        User userB = userService.findByUsername(request.userB());
       Conversation conversation = conversationService.findByUsers(sender,userB);
        service.createMessage(sender,conversation,request.content());
        return ResponseEntity.ok("mensagem enviada");
    }
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editMessageInGroup(@PathVariable UUID id, @RequestBody DirectMenssageRequest request , @AuthenticationPrincipal User requester){
        service.updateMessage(id, request.content(), requester);
        return ResponseEntity.ok("mensagem editada");
    }
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deleteMessageInGroup(@PathVariable UUID id , @AuthenticationPrincipal User requester){
        service.deleteMessage(id,requester);
        return ResponseEntity.ok("messagem apagada");
    }
    @GetMapping("/listar/{id}")
    public ResponseEntity<List<DirectMessageResponse>> listMessagesInGroup(@AuthenticationPrincipal User sender,@PathVariable UUID id){
        User userB = userService.findById(id);
        Conversation conversation = conversationService.findByUsers(sender,userB);
        List<DirectMessage> messagens = service.findByConversation(conversation);
        List<DirectMessageResponse> listar = messagens.stream()
                .map(m -> new DirectMessageResponse(
                        m.getId(),
                        m.getContent(),
                        m.getSender().getUsername(),
                        m.getConversation().getId(),
                        m.getSentAt()
                )).collect(Collectors.toList());
        return ResponseEntity.ok(listar);
}
}