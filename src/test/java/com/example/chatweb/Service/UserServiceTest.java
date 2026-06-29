package com.example.chatweb.Service;
import static org.junit.jupiter.api.Assertions.*;
import com.example.chatweb.entity.User;
import com.example.chatweb.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

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
        assertEquals("usuario ja foi cadastrado", thrown.getMessage());
    }
    @Test
    void deveRetornarOkQuandoUsuarioForEditadoComSucesso(){
       when(userRepository.findById(joao.getId())).thenReturn(Optional.of(joao));
       when(userRepository.findByUsername(joao2.getUsername())).thenReturn(Optional.empty());
       when(userRepository.save(joao)).thenReturn(joao);
       User resultado = userService.updateUser(joao2, joao2.getId());
       assertEquals(joao2.getUsername(), resultado.getUsername());
    }
    @Test
    void deveLancarUmaExcecaoQuandoExistirUsernamesIguais(){
        when(userRepository.findById(joao.getId())).thenReturn(Optional.of(joao));
        when(userRepository.findByUsername(joao2.getUsername())).thenReturn(Optional.of(mateus));

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> { userService.updateUser(joao2,joao2.getId()); }
        );
        assertEquals("username já está em uso", thrown.getMessage());
    }
    @Test
    void deveRetornarUsuarioQuandoEncontrarPeloId(){
        when(userRepository.findById(joao.getId())).thenReturn(Optional.of(joao));
        User userEncontrado = userService.findById(joao.getId());
        assertEquals(joao, userEncontrado);
    }
    @Test
    void deveLancarUmaExcecaoQuandoNaoEncontrarOId(){
        when(userRepository.findById(joao.getId())).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> { userService.findById(joao.getId()); }
        );
        assertEquals("usuario não Encontrado", thrown.getMessage());
    }
    @Test
    void deveRetornarOUserQuandoEncontradoOUsername(){
        when(userRepository.findByUsername(joao.getUsername())).thenReturn(Optional.of(joao));
        User usernameEncontrado = userService.findByUsername(joao.getUsername());
        assertEquals(joao, usernameEncontrado);
    }
    @Test
    void deveLancarUmaExcecaoQuandoOUsernameNaoForEncontrado(){
        when(userRepository.findByUsername(joao.getUsername())).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> { userService.findByUsername(joao.getUsername()); }
        );
        assertEquals("nao encontrado", thrown.getMessage());
    }
    @Test
    void deveRetornarSucessoQuandoOUserForDeletado(){
        when(userRepository.existsById(joao.getId())).thenReturn(true);
        userService.deleteUserById(joao.getId());
        verify(userRepository).deleteById(joao.getId());
    }
    @Test
    void deveLancarUmaExcecaoQuandoOUserNaoForEncontrado(){
        when(userRepository.existsById(joao.getId())).thenReturn(false);
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> { userService.deleteUserById(joao.getId());
                }
        );
        assertEquals("usuario nao encontrado", thrown.getMessage());
    }
    }


