package com.example.chatweb.controller;

import com.example.chatweb.Service.GroupService;
import com.example.chatweb.dto.GroupRequest;
import com.example.chatweb.dto.GroupResponse;
import com.example.chatweb.entity.Group;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.chatweb.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/grupo")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService service;

    @PostMapping("/criar")
    public ResponseEntity<String> createGroup(@RequestBody GroupRequest groupRequest, @AuthenticationPrincipal User owner){
        Group newGroup = new Group(null,groupRequest.name(),owner, null);
        service.createGroup(newGroup,owner);
        return ResponseEntity.ok("grupo criado");
    }
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editGroup(@PathVariable UUID id, @RequestBody GroupRequest groupRequest ,@AuthenticationPrincipal User requester ){
        Group editedGroup = new Group(id,groupRequest.name(),requester,null);
        service.updateGroup(id,editedGroup,requester);
        return ResponseEntity.ok("grupo editado");
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable UUID id,@AuthenticationPrincipal User requester){
        service.deleteGroup(id,requester);
        return ResponseEntity.ok("grupo deletado");
    }
    @GetMapping("/procurar/id/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id){
        Group grupo = service.findById(id);
        return ResponseEntity.ok(new GroupResponse(grupo.getId(),grupo.getName(),grupo.getOwner().getUsername()));
    }
    @GetMapping("/procurar/name/{name}")
    public ResponseEntity<?> findByName(@PathVariable String name){
        Group grupo = service.findByName(name);
        return ResponseEntity.ok(new GroupResponse(grupo.getId(),grupo.getName(),grupo.getOwner().getUsername()));
    }
    @GetMapping("/buscar")
    public ResponseEntity<List<GroupResponse>> findByNameContaining(@RequestParam String name){
        List<Group> grupo = service.findByNameContaining(name);
        List<GroupResponse> listarGrupos = grupo.stream()
                .map(f -> new GroupResponse(
                        f.getId(),
                        f.getName(),
                        f.getOwner().getUsername()
                )).collect(Collectors.toList());
        return ResponseEntity.ok(listarGrupos);
    }
}
