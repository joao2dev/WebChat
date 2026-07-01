package com.example.chatweb.Service;

import com.example.chatweb.entity.Friendship;
import com.example.chatweb.entity.User;
import com.example.chatweb.repositories.FriendshipRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FriendshipService {
    private FriendshipRepository repository;

    Friendship sendFriendRequest(User requester, User addressee){

        if (requester.getId().equals(addressee.getId())){
            throw new RuntimeException("Voce não pode enivar um convite a si mesmo");
        }


        Optional<Friendship> existente = repository.findByRequesterAndAddressee(requester,addressee);
        Optional<Friendship> existenteInverso = repository.findByRequesterAndAddressee(addressee, requester);

        if (existente.isPresent() || existenteInverso.isPresent()) {
            throw new RuntimeException("usuario ja adicionado");
        }

        Friendship novaAmizade = new Friendship(UUID.randomUUID(),requester,addressee, Friendship.FriendshipStatus.PENDING, LocalDateTime.now());
        return repository.save(novaAmizade);
    }
    Friendship acceptFriendRequest(UUID friendshipId){
         Friendship novaAmizade = repository.findById(friendshipId).orElseThrow(() -> new RuntimeException("o usuario nao existe"));
         novaAmizade.setStatus(Friendship.FriendshipStatus.ACCEPTED);
         return repository.save(novaAmizade);
    }
    void declineFriendRequest(UUID friendshipId)  {
        if (!repository.existsById(friendshipId)){
            throw new RuntimeException("usuario nao encontrado");
        }
         repository.deleteById(friendshipId);
    }           // recusar
    /*void deleteFriendship(UUID friendshipId)  {}                     // deletar
    List<Friendship> getFriends(User user){}                        // listar amigos aceitos
    List<Friendship> getPendingRequests(User user)   {}   */          // listar pedidos pendentes recebidos
}
