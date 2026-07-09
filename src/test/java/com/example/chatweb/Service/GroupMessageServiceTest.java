package com.example.chatweb.Service;

import com.example.chatweb.entity.Group;
import com.example.chatweb.entity.GroupMember;
import com.example.chatweb.entity.GroupMessage;
import com.example.chatweb.entity.User;
import com.example.chatweb.repositories.GroupMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
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
class GroupMessageServiceTest {
    private User joao;
    private User maria;
    private Group grupo;
    private GroupMember member;
    private User mario;
    private GroupMessage mensagem;
    private GroupMessage mensagemVazia;
    private GroupMessage mensagemEditada;

    @BeforeEach
    public void setup(){
        this.joao = new User(UUID.randomUUID(), "joao2dev", "joao@gmail.com", "12345");
        this.maria = new User(UUID.randomUUID(), "maria", "maria@gmail.com", "12345");
        this.mario = new User(UUID.randomUUID(), "mario", "mario@gmail.com", "12345");
        this.grupo = new Group(UUID.randomUUID(), "Dev Talk", joao, LocalDateTime.now());
        this.member = new GroupMember(UUID.randomUUID(), maria, grupo, LocalDateTime.now());
        this.mensagem = new GroupMessage(UUID.randomUUID(), "olá galera", joao, grupo, null, LocalDateTime.now());
        this.mensagemEditada = new GroupMessage(mensagem.getId(), "olá galera editado", joao, grupo, null, LocalDateTime.now());
        this.mensagemVazia = new GroupMessage(UUID.randomUUID(), "", joao, grupo, null, LocalDateTime.now());
    }
    @Mock
    private GroupMessageRepository groupMessageRepository;
    @InjectMocks
    private GroupMessageService groupMessageService;

    @Test
    void deveRetornarEnviarUmaMensagemComSucesso(){
        when(groupMessageRepository.save(any())).thenReturn(mensagem);
        GroupMessage newMessage = groupMessageService.createMessage(joao,grupo,"olá galera");
        assertEquals(mensagem.getContent(),newMessage.getContent());
    }
    @Test
    void deveRetornarUmaExcecaoQuandoEnviarUmaMensagemSemConteudo(){
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {groupMessageService.createMessage(joao,grupo,"");
                }
        );
        assertEquals("adicione conteudo a mensagem", thrown.getMessage());
    }
    @Test
    void deveEditarUmaMensagemComSucesso(){
        when(groupMessageRepository.findById(mensagem.getId())).thenReturn(Optional.of(mensagem));
        when(groupMessageRepository.save(any())).thenReturn(mensagemEditada);
        GroupMessage editedMensage = groupMessageService.updateMessage(mensagem.getId(),"olá galera editado",joao);
        assertEquals("olá galera editado",editedMensage.getContent());
    }
    @Test
    void deveLancarUmaExececaoQuandoNaoEncontrarUmaMensagemQueQueiraEditar(){
        when(groupMessageRepository.findById(mensagem.getId())).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {groupMessageService.updateMessage(mensagem.getId(),"olá galera editado",joao);
                }
        );
        assertEquals("mensagem nao encontrada", thrown.getMessage());
    }
    @Test
    void deveLancarUmaExececaoQuandoOutroUsuarioTentarEditarSuaMensagem(){
        when(groupMessageRepository.findById(mensagem.getId())).thenReturn(Optional.of(mensagem));
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {
                    groupMessageService.updateMessage(mensagem.getId(),"olá galera editado",mario);
                }
        );
        assertEquals("voce nao pode editar essa mensagem", thrown.getMessage());
    }
    @Test
    void deveDeletarAMensagemDoGrupoComSucesso(){
        when(groupMessageRepository.findById(mensagem.getId())).thenReturn(Optional.of(mensagem));
        groupMessageService.deleteMessage(mensagem.getId(),joao);
        verify(groupMessageRepository).deleteById(mensagem.getId());
    }
    @Test
    void deveRetornarUmaExececaoQuandoNaoEncontrarAMensagemQueDesejaDeletar(){
        when(groupMessageRepository.findById(mensagem.getId())).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {
                    groupMessageService.deleteMessage(mensagem.getId(),joao);
                }
        );
        assertEquals("mensagem nao encontrada", thrown.getMessage());
    }
    @Test
    void deveRetornarUmaExececaoQuandoAlguemTentarDeletarSuaMensagem(){
        when(groupMessageRepository.findById(mensagem.getId())).thenReturn(Optional.of(mensagem));

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {
                    groupMessageService.deleteMessage(mensagem.getId(),mario);
                }
        );
        assertEquals("voce nao pode apagar essa mensagem", thrown.getMessage());
    }
    @Test
    void deveRetornarAListaDeGruposDoUsuarioComSucesso(){
        when(groupMessageRepository.findByGroupOrderBySentAtAsc(grupo)).thenReturn(List.of(mensagem));
        List<GroupMessage> mensagemDoGrupo = groupMessageService.findByGroup(grupo);
        assertEquals(1,mensagemDoGrupo.size());
    }
}