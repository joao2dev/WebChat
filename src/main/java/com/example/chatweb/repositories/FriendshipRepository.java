package com.example.chatweb.repositories;

import com.example.chatweb.entity.Friendship;
import com.example.chatweb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendshipRepository extends JpaRepository<Friendship, UUID> {
    List<Friendship> findByRequester(User requester);
    List<Friendship> findByAddressee(User addressee);
    Optional<Friendship> findByRequesterAndAddressee(User requester, User addressee);
    List<Friendship> findByAddresseeAndStatus(User addressee, Friendship.FriendshipStatus status);

}
