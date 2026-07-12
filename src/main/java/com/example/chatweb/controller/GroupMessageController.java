package com.example.chatweb.controller;

import com.example.chatweb.Service.GroupMessageService;
import com.example.chatweb.Service.GroupService;
import com.example.chatweb.dto.GroupMessageRequest;
import com.example.chatweb.dto.GroupMessageResponse;
import com.example.chatweb.entity.Group;
import com.example.chatweb.entity.GroupMessage;
import com.example.chatweb.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/group-message")
@RequiredArgsConstructor
public class GroupMessageController {
    private final GroupMessageService service;
    private final GroupService groupService;

    @PostMapping("/enviar")
    public ResponseEntity<?> sendMessageInGroup(@AuthenticationPrincipal User sender , @RequestBody GroupMessageRequest request){
        Group grupo = groupService.findByName(request.groupName());
        service.createMessage(sender,grupo,request.content());
        return ResponseEntity.ok("mensagem enviada");
    }
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editMessageInGroup(@PathVariable UUID id,@RequestBody GroupMessageRequest request , @AuthenticationPrincipal User requester){
        service.updateMessage(id, request.content(), requester);
        return ResponseEntity.ok("mensagem editada");
    }
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deleteMessageInGroup(@PathVariable UUID id , @AuthenticationPrincipal User requester){
        service.deleteMessage(id,requester);
        return ResponseEntity.ok("messagem apagada");
    }
    @GetMapping("/listar/{groupId}")
    public ResponseEntity<List<GroupMessageResponse>> listMessagesInGroup(@PathVariable UUID groupId){
        Group grupo = groupService.findById(groupId);
        List<GroupMessage> messagens = service.findByGroup(grupo);
        List<GroupMessageResponse> listar = messagens.stream()
                .map(m -> new GroupMessageResponse(
                        m.getId(),
                        m.getContent(),
                        m.getSender().getUsername(),
                        m.getGroup().getName(),
                        m.getSentAt()
                )).collect(Collectors.toList());
        return ResponseEntity.ok(listar);
    }
}
