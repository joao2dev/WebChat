package com.example.chatweb.Service;
import com.example.chatweb.entity.Group;
import com.example.chatweb.entity.User;
import com.example.chatweb.repositories.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GroupService {
    private GroupRepository repository;


    Group createGroup(Group group, User owner){
        if (group.getName() == null || group.getName().isBlank()) {
            throw new RuntimeException("o nome do grupo esta vazio");
        }
        group.setOwner(owner);
        return repository.save(group);
    }
    Group updateGroup(UUID id, Group group, User requester){
        Group grupoExistente =  repository.findById(id).orElseThrow(() ->new RuntimeException("grupo nao encontrado"));
        if (!grupoExistente.getOwner().getId().equals(requester.getId())){
            throw new RuntimeException("voce nao tem permissao para isso");
        }
        grupoExistente.setName(group.getName());
        return repository.save(grupoExistente);

    }
    void deleteGroup(UUID id, User requester){
        Group grupoExistente = repository.findById(id).orElseThrow(() -> new RuntimeException("grupo nao encontrado"));
        if (!grupoExistente.getOwner().getId().equals(requester.getId())){
            throw new RuntimeException("Voce nao tem permissao para isso");
        }
        repository.deleteById(grupoExistente.getId());
    }
    Group findById(UUID id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("grupo nao encontrado"));
    }
    Group findByName(String name){
        return repository.findByName(name).orElseThrow(() -> new RuntimeException("grupo nao encontrado"));
    }
    List<Group> findByNameContaining(String name){
        return repository.findByNameContaining(name);
    }
}
