package com.example.chatweb.Service;

import com.example.chatweb.entity.*;
import com.example.chatweb.repositories.DirectMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Service
@AllArgsConstructor
public class DirectMessageService {

    private final DirectMessageRepository repository;

    public DirectMessage createMessage(User sender, Conversation conversation, String content){
        if ( content == null || content.isBlank() ){
            throw new RuntimeException("adicione conteudo a mensagem");
        }
        DirectMessage newMessage = new DirectMessage(UUID.randomUUID(),content,sender,conversation, LocalDateTime.now() ,null);
        return repository.save(newMessage);
    }
    public DirectMessage updateMessage(UUID id, String content, User requester){
        DirectMessage mensagemEncontrada = repository.findById(id).orElseThrow(() -> new RuntimeException("mensagem nao encontrada"));
        if (!mensagemEncontrada.getSender().getId().equals(requester.getId())){
            throw new RuntimeException("voce nao pode editar essa mensagem");
        }
        mensagemEncontrada.setContent(content);
        return repository.save(mensagemEncontrada);
    }
    public void deleteMessage(UUID id, User requester){
        DirectMessage mensagemEncontrada = repository.findById(id).orElseThrow(() -> new RuntimeException("mensagem nao encontrada"));
        if (!mensagemEncontrada.getSender().getId().equals(requester.getId())){
            throw new RuntimeException("voce nao pode apagar essa mensagem");
        }
        repository.deleteById(mensagemEncontrada.getId());
    }
    public List<DirectMessage> findByConversation(Conversation conversation){
        return repository.findByConversationOrderBySentAtAsc(conversation);
    }
}
