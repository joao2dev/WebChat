package com.example.chatweb.Service;

import com.example.chatweb.entity.User;
import com.example.chatweb.repositories.UserRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
   private final UserRepository repository;

   public User createUser(User user){
       if (repository.findByUsername(user.getUsername()).isPresent()){
           throw new RuntimeException("usuario ja foi cadastrado");
       }
        return repository.save(user);
   }
   public User updateUser(User user,UUID id){
       User userExistente = repository.findById(id).orElseThrow(() -> new RuntimeException("nao enocntrado"));
       if (!userExistente.getUsername().equals(user.getUsername()) && repository.findByUsername(user.getUsername()).isPresent()){
           throw new RuntimeException("username já está em uso");
       }
       if (!userExistente.getEmail().equals(user.getEmail())&& repository.findByEmail(user.getEmail()).isPresent()){
           throw new RuntimeException("email já está em uso");
       }

       userExistente.setUsername(user.getUsername());
       userExistente.setEmail(user.getEmail());
       //TODO: aplicar passwordEnocnder quando terminar o spring Security
       userExistente.setPassword(user.getPassword());
       return repository.save(userExistente);
   }
   public User findById(UUID id) {
       return repository.findById(id)
               .orElseThrow(() -> new RuntimeException("nao encontrado"));
   }
   public User findByUsername(String username){
       return repository.findByUsername(username).orElseThrow(() -> new RuntimeException("nao encontrado"));
   }
   public void deleteUserById(UUID id){
       if (!repository.existsById(id)) {
           throw new RuntimeException("usuario nao encontrado");
       }
        repository.deleteById(id);
   }

}
