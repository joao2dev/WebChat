package com.example.chatweb.Service;
import static org.junit.jupiter.api.Assertions.*;
import com.example.chatweb.entity.User;
import com.example.chatweb.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private User joao;
    private User joao2;
    private User mateus;

    @BeforeEach
    public void criarUsuario(){
        this.joao = new User(UUID.randomUUID(),"joao2dev","joao@gmail.com","12345");
        this.joao2 = new User(joao.getId(),"joao23dev","joao2@gmail.com","12346");
        this.mateus = new User(UUID.randomUUID(),"matheus","mateus@gmail.com","12345c");
    }

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void deveLancarExcecaoQuandoUsernameJaExiste() {
        when((userRepository.findByUsername(joao.getUsername()))).thenReturn(Optional.of(joao));
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> { userService.createUser(joao); }
        );
        assertEquals("usuario ja esta cadastrado", thrown.getMessage());
    }
    @Test
    void deveMostraroAsAlteracoesQuandoExecutadoComSucesso(){
       when(userRepository.findById(joao.getId())).thenReturn(Optional.of(joao));
       when(userRepository.findByUsername(joao2.getUsername())).thenReturn(Optional.empty());
       when(userRepository.findByEmail(joao2.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(joao)).thenReturn(joao);
       User resultado = userService.updateUser(joao2, joao2.getId());
       assertEquals(joao2.getUsername(), resultado.getUsername());
    }
    }
