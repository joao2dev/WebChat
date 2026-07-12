package com.example.chatweb.controller;

import com.example.chatweb.Service.GroupMemberService;
import com.example.chatweb.Service.GroupService;
import com.example.chatweb.Service.UserService;
import com.example.chatweb.dto.GroupMemberRequest;
import com.example.chatweb.dto.GroupMemberResponse;
import com.example.chatweb.entity.Group;
import com.example.chatweb.entity.GroupMember;
import com.example.chatweb.entity.User;
import com.example.chatweb.repositories.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("member")
@RequiredArgsConstructor
public class GroupMemberController {
    private final GroupMemberService service;
    private final UserService userService;
    private final GroupService groupService;
    private final GroupMemberRepository repository;

    @PostMapping("/add")
    public ResponseEntity<?> addMember(@RequestBody GroupMemberRequest groupMemberRequest, @AuthenticationPrincipal User requester){
       User user = userService.findByUsername(groupMemberRequest.username());
       Group grupo =groupService.findByName(groupMemberRequest.groupName());
       service.addMember(user,grupo);
       return ResponseEntity.ok("membro adicionado");
    }
    @DeleteMapping("/remover")
    public ResponseEntity<?> removeMember(
            @RequestBody GroupMemberRequest request,
            @AuthenticationPrincipal User requester) {
        User user = userService.findByUsername(request.username());
        Group grupo =groupService.findByName(request.groupName());
        GroupMember member = repository.findByUserAndGroup(user, grupo)
                .orElseThrow(() -> new RuntimeException("membro nao encontrado"));
        service.removeMember(member,requester);
        return ResponseEntity.ok("membro removido");
    }
    @GetMapping("/procurar/{groupId}")
    public ResponseEntity<List<GroupMemberResponse>> ListMembers(@PathVariable UUID groupId){
        Group group = groupService.findById(groupId);
        List<GroupMember> membros = service.findMembers(group);
        List<GroupMemberResponse> listar = membros.stream()
                .map(m -> new GroupMemberResponse(
                        m.getId(),
                        m.getUser().getUsername(),
                        m.getGroup().getName()
                )).collect(Collectors.toList());
        return ResponseEntity.ok(listar);

    }

}
