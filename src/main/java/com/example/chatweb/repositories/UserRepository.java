package com.example.chatweb.repositories;

import com.example.chatweb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID > {

    Optional<User> findByUsername(String username);
    List<User> findByUsernameContaining(String username);
    Optional<User> findByEmail(String email);
}
