package com.example.chatweb.Service;

import com.example.chatweb.entity.Group;
import com.example.chatweb.entity.GroupMessage;
import com.example.chatweb.entity.User;
import com.example.chatweb.repositories.GroupMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GroupMessageService {

    private final GroupMessageRepository repository;

    GroupMessage createMessage(User sender, Group group, String content){
        if ( content == null || content.isBlank() ){
            throw new RuntimeException("adicione conteudo a mensagem");
        }
        GroupMessage newMessage = new GroupMessage(UUID.randomUUID(),content,sender,group,LocalDateTime.now() ,null);
        return repository.save(newMessage);
    }
    GroupMessage updateMessage(UUID id, String content, User requester){
        GroupMessage mensagemEncontrada = repository.findById(id).orElseThrow(() -> new RuntimeException("mensagem nao encontrada"));
        if (!mensagemEncontrada.getSender().getId().equals(requester.getId())){
            throw new RuntimeException("voce nao pode editar essa mensagem");
        }
        mensagemEncontrada.setContent(content);
        return repository.save(mensagemEncontrada);
    }
    void deleteMessage(UUID id, User requester){
        GroupMessage mensagemEncontrada = repository.findById(id).orElseThrow(() -> new RuntimeException("mensagem nao encontrada"));
        if (!mensagemEncontrada.getSender().getId().equals(requester.getId())){
            throw new RuntimeException("voce nao pode apagar essa mensagem");
        }
        repository.deleteById(mensagemEncontrada.getId());
    }
    List<GroupMessage> findByGroup(Group group){
        return repository.findByGroupOrderBySentAtAsc(group);
    }
}
