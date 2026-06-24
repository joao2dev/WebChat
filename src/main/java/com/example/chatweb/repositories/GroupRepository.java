package com.example.chatweb.repositories;
import com.example.chatweb.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, UUID> {
    Optional<Group> findByName(String name);
    List<Group> findByNameContaining(String name);
}
