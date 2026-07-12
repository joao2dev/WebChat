package com.example.chatweb.controller;

import com.example.chatweb.Service.FriendshipService;
import com.example.chatweb.Service.UserService;
import com.example.chatweb.dto.FriendshipRequest;
import com.example.chatweb.dto.FriendshipResponse;
import com.example.chatweb.dto.UserRequest;
import com.example.chatweb.dto.UserResponse;
import com.example.chatweb.entity.Friendship;
import com.example.chatweb.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/friendship")
@RequiredArgsConstructor
public class FriendshipController {
    private final FriendshipService service;
    private final UserService userService;

    @PostMapping("/enviar")
    public ResponseEntity<String> sendInvite(
            @RequestBody FriendshipRequest request,
            @AuthenticationPrincipal User requester) {
        User addressee = userService.findById(request.addresseeId());
        service.sendFriendRequest(requester, addressee);
        return ResponseEntity.ok("pedido enviado");
    }
    @PutMapping("/aceitar/{id}")
    public ResponseEntity<?> acceptInvite(@PathVariable UUID id ){
        service.acceptFriendRequest(id);
        return ResponseEntity.ok("pedido aceito");
    }
    @PutMapping("/recusar/{id}")
    public ResponseEntity<?> declineInvite(@PathVariable UUID id){
        service.declineFriendRequest(id);
        return ResponseEntity.ok("pedido recusado");
    }
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deleteFriendship(@PathVariable UUID id){
        service.deleteFriendship(id);
        return ResponseEntity.ok("amizade deletada");
    }
    @GetMapping("/listar/amigos")
    public ResponseEntity<List<FriendshipResponse>> getFriends(@AuthenticationPrincipal User request){
        List<Friendship> amizades = service.getFriends(request);
        List<FriendshipResponse> listaDeAmigos = amizades.stream()
                .map(f -> new FriendshipResponse(
                        f.getId(),
                        f.getRequester().getUsername(),
                        f.getAddressee().getUsername(),
                        f.getStatus()
                )).collect(Collectors.toList());
        return ResponseEntity.ok(listaDeAmigos);
    }
    @GetMapping("/pendentes")
    public ResponseEntity<List<FriendshipResponse>> getPendingInvites(@AuthenticationPrincipal User request){
        List<Friendship> pedidosPendentes = service.getPendingRequests(request);
        List<FriendshipResponse> listaPendentes = pedidosPendentes.stream()
                .map(f -> new FriendshipResponse(
                        f.getId(),
                        f.getRequester().getUsername(),
                        f.getAddressee().getUsername(),
                        f.getStatus()
                )).collect(Collectors.toList());
        return ResponseEntity.ok(listaPendentes);
    }
}
