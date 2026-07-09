package com.example.chatweb.Service;

import com.example.chatweb.entity.Conversation;
import com.example.chatweb.entity.DirectMessage;
import com.example.chatweb.entity.User;
import com.example.chatweb.repositories.DirectMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectMessageServiceTest {
    private User joao;
    private User maria;
    private Conversation joaoEMaria;
    private DirectMessage mensagem;
    private DirectMessage mensagemEditada;

    @BeforeEach
    void setup(){
        this.joao = new User(UUID.randomUUID(), "joao2dev", "joao@gmail.com", "12345");
        this.maria = new User(UUID.randomUUID(), "maria", "maria@gmail.com", "12345");
        this.joaoEMaria = new Conversation(UUID.randomUUID(),joao,maria, LocalDateTime.now());
        this.mensagem = new DirectMessage(UUID.randomUUID(), "oi maria", joao, joaoEMaria, null, LocalDateTime.now());
        this.mensagemEditada = new DirectMessage(mensagem.getId(), "oi maria editado", joao, joaoEMaria, null, LocalDateTime.now());
    }
    @Mock
    private DirectMessageRepository directMessageRepository;

    @InjectMocks
    private DirectMessageService directMessageService;


    @Test
    void deveRetornarEnviarUmaMensagemComSucesso(){
        when(directMessageRepository.save(any())).thenReturn(mensagem);
        DirectMessage newMessage = directMessageService.createMessage(joao,joaoEMaria,"oi maria");
        assertEquals(mensagem.getContent(),newMessage.getContent());
    }
    @Test
    void deveRetornarUmaExcecaoQuandoEnviarUmaMensagemSemConteudo(){
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {directMessageService.createMessage(joao,joaoEMaria,"");
                }
        );
        assertEquals("adicione conteudo a mensagem", thrown.getMessage());
    }
    @Test
    void deveEditarUmaMensagemComSucesso(){
        when(directMessageRepository.findById(mensagem.getId())).thenReturn(Optional.of(mensagem));
        when(directMessageRepository.save(any())).thenReturn(mensagemEditada);
        DirectMessage editedMensage = directMessageService.updateMessage(mensagem.getId(),"oi maria editado",joao);
        assertEquals("oi maria editado",editedMensage.getContent());
    }
    @Test
    void deveLancarUmaExececaoQuandoNaoEncontrarUmaMensagemQueQueiraEditar(){
        when(directMessageRepository.findById(mensagem.getId())).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {directMessageService.updateMessage(mensagem.getId(),"oi maria editado",joao);
                }
        );
        assertEquals("mensagem nao encontrada", thrown.getMessage());
    }
    @Test
    void deveLancarUmaExececaoQuandoOutroUsuarioTentarEditarSuaMensagem(){
        when(directMessageRepository.findById(mensagem.getId())).thenReturn(Optional.of(mensagem));
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {
                    directMessageService.updateMessage(mensagem.getId(),"oi maria editado",maria);
                }
        );
        assertEquals("voce nao pode editar essa mensagem", thrown.getMessage());
    }
    @Test
    void deveDeletarAMensagemDoGrupoComSucesso(){
        when(directMessageRepository.findById(mensagem.getId())).thenReturn(Optional.of(mensagem));
        directMessageService.deleteMessage(mensagem.getId(),joao);
        verify(directMessageRepository).deleteById(mensagem.getId());
    }
    @Test
    void deveRetornarUmaExececaoQuandoNaoEncontrarAMensagemQueDesejaDeletar(){
        when(directMessageRepository.findById(mensagem.getId())).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {
                    directMessageService.deleteMessage(mensagem.getId(),joao);
                }
        );
        assertEquals("mensagem nao encontrada", thrown.getMessage());
    }
    @Test
    void deveRetornarUmaExececaoQuandoAlguemTentarDeletarSuaMensagem(){
        when(directMessageRepository.findById(mensagem.getId())).thenReturn(Optional.of(mensagem));

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {
                    directMessageService.deleteMessage(mensagem.getId(),maria);
                }
        );
        assertEquals("voce nao pode apagar essa mensagem", thrown.getMessage());
    }
    @Test
    void deveRetornarAListaDeGruposDoUsuarioComSucesso(){
        when(directMessageRepository.findByConversationOrderBySentAtAsc(joaoEMaria)).thenReturn(List.of(mensagem));
        List<DirectMessage> mensagens = directMessageService.findByConversation(joaoEMaria);
        assertEquals(1, mensagens.size());
    }

}