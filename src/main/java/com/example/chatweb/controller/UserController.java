package com.example.chatweb.controller;
import com.example.chatweb.entity.User;
import com.example.chatweb.Service.UserService;
import com.example.chatweb.dto.UserRequest;
import com.example.chatweb.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editUser(@RequestBody UserRequest userRequest,@PathVariable UUID id){
       User updatedUser = new User(null,userRequest.username(),userRequest.email(),userRequest.password());
       service.updateUser(updatedUser,id);
       return ResponseEntity.ok("alterações concluidas");
    }
    @GetMapping("procurar/id/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id){
        User user = service.findById(id);
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getUsername(), user.getEmail()));
    }
    @GetMapping("procurar/{username}")
    public ResponseEntity<?> findByUsername(@PathVariable String username){
        User user = service.findByUsername(username);
        return ResponseEntity.ok(new UserResponse(user.getId(),username,user.getEmail()));
    }
    @DeleteMapping("deletar/{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id){
        service.deleteUserById(id);
        return ResponseEntity.ok("usuario deletado");
    }
}
