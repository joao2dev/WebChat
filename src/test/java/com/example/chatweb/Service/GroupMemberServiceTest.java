package com.example.chatweb.Service;

import com.example.chatweb.entity.Group;
import com.example.chatweb.entity.GroupMember;
import com.example.chatweb.repositories.GroupMemberRepository;
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
class GroupMemberServiceTest {

    private User joao;
    private User maria;
    private Group grupo;
    private GroupMember member;
    private User mario;

    @BeforeEach
    public void setup(){
        this.joao = new User(UUID.randomUUID(), "joao2dev", "joao@gmail.com", "12345");
        this.maria = new User(UUID.randomUUID(), "maria", "maria@gmail.com", "12345");
        this.mario = new User(UUID.randomUUID(), "mario", "mario@gmail.com", "12345");
        this.grupo = new Group(UUID.randomUUID(), "Dev Talk", joao, LocalDateTime.now());
        this.member = new GroupMember(UUID.randomUUID(), maria, grupo, LocalDateTime.now());
    }

    @InjectMocks
    private GroupMemberService groupMemberService;

    @Mock
    private GroupMemberRepository groupMemberRepository;

    @Test
    void deveRetornarSucessoAoAdicionarUmMembro(){
        when(groupMemberRepository.existsByUserAndGroup(maria, grupo)).thenReturn(false);
        when(groupMemberRepository.save(any())).thenReturn(member);

        GroupMember novoMembro = groupMemberService.addMember(maria,grupo);


        assertEquals(member.getUser(),novoMembro.getUser());
        assertEquals(member.getGroup(),novoMembro.getGroup());
    }
    @Test
    void deveLancarUmaExcecaoCasoTenteAddUmUusarioQueJaEstaNoGrupo(){
        when(groupMemberRepository.existsByUserAndGroup(member.getUser(),grupo)).thenReturn(true);
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {groupMemberService.addMember(member.getUser(),grupo);
                }
        );
        assertEquals("o membro ja esta presente no grupo", thrown.getMessage());
    }
    @Test
    void deveRetornarSucessoAoRemoverUmMembroDoGrupo(){
        when(groupMemberRepository.existsByUserAndGroup(member.getUser(),grupo)).thenReturn(true);
        groupMemberService.removeMember(member,joao);
        verify(groupMemberRepository).deleteById(member.getId());
    }
    @Test
    void deveLancarUmaExcecaoQuandoUmmewmbroNaoExistirNoGrupo(){
        when(groupMemberRepository.existsByUserAndGroup(member.getUser(),grupo)).thenReturn(false);
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {groupMemberService.removeMember(member,joao);
                }
        );
        assertEquals("esse membro nao existe", thrown.getMessage());
    }
    @Test
    void deveLancarUmaExcecaoQuandoUmNaoAdministradorTentarRemoverUmMembro(){
        when(groupMemberRepository.existsByUserAndGroup(member.getUser(),grupo)).thenReturn(true);
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {groupMemberService.removeMember(member,mario);
                }
        );
        assertEquals("Voce nao e um administrador", thrown.getMessage());
    }
    @Test
    void deveRetornarSucessoAoListarOsMembrosDoGrupo(){
        when(groupMemberRepository.findByGroup(grupo)).thenReturn(List.of(member));
        List<GroupMember> membrosDoGrupo = groupMemberService.findMembers(grupo);
        assertEquals(1,membrosDoGrupo.size());
    }
}
