package com.example.chatweb.Service;

import com.example.chatweb.entity.Friendship;
import com.example.chatweb.entity.User;
import com.example.chatweb.repositories.FriendshipRepository;
import com.example.chatweb.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FriendshipServiceTest {

    private User joao;
    private User maria;

    @BeforeEach
    public void setup(){
        this.joao = new User(UUID.randomUUID(), "joao2dev", "joao@gmail.com", "12345");
        this.maria = new User(UUID.randomUUID(), "maria", "maria@gmail.com", "12345");
    }

    @Mock
    private FriendshipRepository friendshipRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FriendshipService friendshipService;

    @Test
    void deveLancarExcecaoQuandoUsuarioTentaAdicionarASiMesmo(){
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {
                    friendshipService.sendFriendRequest(joao,joao);
                }

        );
        assertEquals("Voce não pode enivar um convite a si mesmo",thrown.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoJaExisteFriendship(){
        when(friendshipRepository.findByRequesterAndAddressee(joao,maria)).thenReturn(Optional.of(new Friendship(UUID.randomUUID(),joao,maria, Friendship.FriendshipStatus.PENDING, LocalDateTime.now())));
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {friendshipService.sendFriendRequest(joao,maria);
                }
        );
        assertEquals("usuario ja adicionado",thrown.getMessage());
    }

    @Test
    void deveCriarFriendshipComSucesso(){
        Friendship friendshipEsperada = new Friendship(
                UUID.randomUUID(), joao, maria,
                Friendship.FriendshipStatus.PENDING,
                LocalDateTime.now()
        );
        when(friendshipRepository.findByRequesterAndAddressee(joao,maria)).thenReturn(Optional.empty());
        when(friendshipRepository.save(any())).thenReturn(friendshipEsperada);
        Friendship amizade = friendshipService.sendFriendRequest(joao,maria);

        assertEquals(joao, amizade.getRequester());
        assertEquals(maria, amizade.getAddressee());
        assertEquals(Friendship.FriendshipStatus.PENDING, amizade.getStatus());
    }
    @Test
    void deveAceitarFriendshipComSuceso(){
        Friendship pendente = new Friendship(
                UUID.randomUUID(), joao, maria,
                Friendship.FriendshipStatus.PENDING,
                LocalDateTime.now()
        );
        Friendship amizadeAceita = new Friendship(
                UUID.randomUUID(), joao, maria,
                Friendship.FriendshipStatus.ACCEPTED,
                LocalDateTime.now()
        );
        when(friendshipRepository.findById(pendente.getId())).thenReturn(Optional.of(pendente));
        when(friendshipRepository.save(any())).thenReturn(amizadeAceita);

        Friendship novaAmizade = friendshipService.acceptFriendRequest(pendente.getId());
        assertEquals(Friendship.FriendshipStatus.ACCEPTED,novaAmizade.getStatus());
    }
    @Test
    void deveLancarUmaExcecaoSeOIdNaoExistirNoBanco(){
        Friendship pedido = new Friendship(
                UUID.randomUUID(), joao, maria,
                Friendship.FriendshipStatus.PENDING,
                LocalDateTime.now()
        );

        when(friendshipRepository.findById(pedido.getId())).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {friendshipService.acceptFriendRequest(pedido.getId());
                }
        );
        assertEquals("o usuario nao existe", thrown.getMessage());
    }
    @Test
    void deveRecusarConviteComSucesso(){
        Friendship pendente = new Friendship(
                UUID.randomUUID(), joao, maria,
                Friendship.FriendshipStatus.PENDING,
                LocalDateTime.now()
        );
        when(friendshipRepository.existsById(pendente.getId())).thenReturn(true);
        friendshipService.declineFriendRequest(pendente.getId());
        verify(friendshipRepository).deleteById(pendente.getId());

    }
    @Test
    void deveLancarUmaExcecaoCasoOIdNaoExiste(){
        Friendship pendente = new Friendship(
                UUID.randomUUID(), joao, maria,
                Friendship.FriendshipStatus.PENDING,
                LocalDateTime.now()
        );
        when(friendshipRepository.existsById(pendente.getId())).thenReturn(false);
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> {friendshipService.declineFriendRequest(pendente.getId());
                }
        );
        assertEquals("usuario nao encontrado",thrown.getMessage());

    }
}