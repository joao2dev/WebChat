package com.example.chatweb.Service;

import com.example.chatweb.entity.Conversation;
import com.example.chatweb.entity.User;
import com.example.chatweb.repositories.ConversationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConversationServiceTest {
    private User joao;
    private User maria;
    private Conversation joaoEMaria;

    @BeforeEach
    void setup(){
        this.joao = new User(UUID.randomUUID(), "joao2dev", "joao@gmail.com", "12345");
        this.maria = new User(UUID.randomUUID(), "maria", "maria@gmail.com", "12345");
        this.joaoEMaria = new Conversation(UUID.randomUUID(),joao,maria, LocalDateTime.now());
    }
    @Mock
    private ConversationRepository conversationRepository;

    @InjectMocks
    private ConversationService conversationService;

    @Test
    void deveRetornarSucessoAoCriarUmaConversa(){
        when(conversationRepository.findByUserAAndUserB(joao,maria)).thenReturn(Optional.empty());
        when(conversationRepository.findByUserAAndUserB(maria,joao)).thenReturn(Optional.empty());
        when(conversationRepository.save(any())).thenReturn(joaoEMaria);
        Conversation newChat = conversationService.createConversation(joao,maria);
        assertEquals(joao, newChat.getUserA());
        assertEquals(maria, newChat.getUserB());
    }
    @Test
    void deveLancarExececaoQuandoUmaConversaJaExiste(){
        when(conversationRepository.findByUserAAndUserB(joao,maria)).thenReturn(Optional.of(joaoEMaria));
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {conversationService.createConversation(joao,maria);
                }
        );
        assertEquals("essa conversa ja existe", thrown.getMessage());
    }
    @Test
    void deveRetornarSucessoAoBuscarUmaConversa(){
        when(conversationRepository.findByUserAAndUserB(joao,maria)).thenReturn(Optional.of(joaoEMaria));

        Conversation chat = conversationService.findByUsers(joao,maria);

        assertEquals(joaoEMaria,chat);
    }
    @Test
    void deveLancarExececaoQuandoNaoEnontrarConversa(){
        when(conversationRepository.findByUserAAndUserB(joao,maria)).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {conversationService.findByUsers(joao,maria);
                }
        );
        assertEquals("conversa nao encontrada", thrown.getMessage());
    }
}