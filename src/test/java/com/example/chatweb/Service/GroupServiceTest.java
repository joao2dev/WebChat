package com.example.chatweb.Service;

import com.example.chatweb.entity.Group;
import com.example.chatweb.entity.User;
import com.example.chatweb.repositories.GroupRepository;
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
class GroupServiceTest {

    private User joao;
    private Group grupo;
    private Group grupoSemNome;
    private Group grupoEditado;
    private User maria;

    @BeforeEach
    public void setup(){
        this.joao = new User(UUID.randomUUID(), "joao2dev", "joao@gmail.com", "12345");
        this.maria = new User(UUID.randomUUID(), "maria", "maria@gmail.com", "12345");
        this.grupo = new Group(UUID.randomUUID(), "Dev Talk", joao, LocalDateTime.now());
        this.grupoEditado = new Group(grupo.getId(), "Dev Talks", joao, LocalDateTime.now());
         this.grupoSemNome = new Group(UUID.randomUUID(), "", joao, LocalDateTime.now());
    }

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private GroupService groupService;

    @Test
    void deveCriarGrupoComSucesso(){
        when(groupRepository.save(grupo)).thenReturn(grupo);
        Group newGroup = groupService.createGroup(grupo,joao);
        assertEquals("Dev Talk", newGroup.getName());
    }

    @Test
    void deveLancarExcecaoQuandoNomeDoGrupoEstiverVazio(){
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {groupService.createGroup(grupoSemNome,joao);
                }
        );
        assertEquals("o nome do grupo esta vazio", thrown.getMessage());
    }
    @Test
    void deveRetornarSucessoAoFazerUpdate(){
        when(groupRepository.findById(grupo.getId())).thenReturn(Optional.of(grupo));
        when(groupRepository.save(any())).thenReturn(grupoEditado);
        Group edited = groupService.updateGroup(grupo.getId(), grupoEditado, joao);
        assertEquals(grupoEditado.getName(),edited.getName());
    }
    @Test
    void develancarExcecaoQuandoNaoEncontrarGrupo(){
        when(groupRepository.findById(grupo.getId())).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {groupService.updateGroup(grupo.getId(),grupo,joao);
                }
        );
        assertEquals("grupo nao encontrado",thrown.getMessage());
    }
    @Test
    void develancarUmaExcecaoQuandoUmNaoAdministradorTentarEditar(){
        when(groupRepository.findById(grupo.getId())).thenReturn(Optional.of(grupo));
            RuntimeException thrown = assertThrows(
                    RuntimeException.class,
                    () -> {groupService.updateGroup(grupo.getId(),grupo,maria);
                    }
            );
            assertEquals("voce nao tem permissao para isso",thrown.getMessage());
        }
        @Test
        void deveRetornarSucessoAoDeletarumGrupo(){
            when(groupRepository.findById(grupo.getId())).thenReturn(Optional.of(grupo));
            groupService.deleteGroup(grupo.getId(),joao);
            verify(groupRepository).deleteById(grupo.getId());
        }
        @Test
        void deveLancarUmaExcecaoQuandoNaoEncontrarNaoEncontrarUmGrupo(){
            when(groupRepository.findById(grupo.getId())).thenReturn(Optional.empty());
            RuntimeException thrown = assertThrows(
                    RuntimeException.class,
                    () -> {groupService.deleteGroup(grupo.getId(),joao);
                    }
            );
            assertEquals("grupo nao encontrado", thrown.getMessage());
    }
    @Test
    void develancarUmaExececaoQuandoUmNaoAdministradorTentarDeletarUmGrupo(){
        when(groupRepository.findById(grupo.getId())).thenReturn(Optional.of(grupo));
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {groupService.deleteGroup(grupo.getId(),maria);
                }
        );
        assertEquals("Voce nao tem permissao para isso",thrown.getMessage());
    }
    @Test
    void deveTerSucessoAoEncontrarUmGrupoPleoId(){
        when(groupRepository.findById(grupo.getId())).thenReturn(Optional.of(grupo));
        Group grupoExistente = groupService.findById(grupo.getId());
        assertEquals(grupo,grupoExistente);
    }
    @Test
    void deveLancarUmaExececaoQuandonaoEncontrarGrupoPeloId(){
        when(groupRepository.findById(grupo.getId())).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {groupService.findById(grupo.getId());
                }
        );
        assertEquals("grupo nao encontrado",thrown.getMessage());
    }
    @Test
    void deveRetornarSucessoAoEncontrarUmGrupoPeloNome(){
        when(groupRepository.findByName(grupo.getName())).thenReturn(Optional.of(grupo));
        Group grupoEncontrado = groupService.findByName(grupo.getName());
        assertEquals(grupo.getName(), grupoEncontrado.getName());
    }
    @Test
    void deveLancarUmaExececaoQuandoNaoEncontrarNomeDoGrupo(){
        when(groupRepository.findByName(grupo.getName())).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {groupService.findByName(grupo.getName());
                }
        );
        assertEquals("grupo nao encontrado",thrown.getMessage());
    }
    @Test
    void deveRetornarAoFazerBuscarParcilPelosNomePesquisado(){
        when(groupRepository.findByNameContaining(grupo.getName())).thenReturn(List.of(grupo));
        List<Group> resultado = groupService.findByNameContaining(grupo.getName());
        assertEquals(List.of(grupo),resultado);
    }

    }
