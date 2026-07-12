package com.example.chatweb.Service;

import com.example.chatweb.entity.Friendship;
import com.example.chatweb.entity.User;
import com.example.chatweb.repositories.FriendshipRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class FriendshipService {
    private final FriendshipRepository repository;

    public Friendship sendFriendRequest(User requester, User addressee){

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
    public Friendship acceptFriendRequest(UUID friendshipId){
         Friendship novaAmizade = repository.findById(friendshipId).orElseThrow(() -> new RuntimeException("o usuario nao existe"));
         novaAmizade.setStatus(Friendship.FriendshipStatus.ACCEPTED);
         return repository.save(novaAmizade);
    }
    public void declineFriendRequest(UUID friendshipId)  {
        if (!repository.existsById(friendshipId)){
            throw new RuntimeException("usuario nao encontrado");
        }
         repository.deleteById(friendshipId);
    }           // recusar
    public void deleteFriendship(UUID friendshipId)  {
        if (!repository.existsById(friendshipId)){
            throw new RuntimeException("usuario nao encontrado");
        }
        repository.deleteById(friendshipId);
    }
    public List<Friendship> getFriends(User user){
        List<Friendship> addresseeList = repository.findByAddresseeAndStatus(user, Friendship.FriendshipStatus.ACCEPTED);
        List<Friendship> requesterList = repository.findByRequesterAndStatus(user, Friendship.FriendshipStatus.ACCEPTED);
        return Stream.concat(requesterList.stream(),addresseeList.stream()).collect(Collectors.toList());
    }
    public List<Friendship> getPendingRequests(User user) {
        return repository.findByAddresseeAndStatus(user, Friendship.FriendshipStatus.PENDING);

     }
}
