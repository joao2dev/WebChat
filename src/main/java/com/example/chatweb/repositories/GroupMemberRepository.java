package com.example.chatweb.repositories;

import com.example.chatweb.entity.Group;
import com.example.chatweb.entity.GroupMember;
import com.example.chatweb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupMemberRepository extends JpaRepository<GroupMember, UUID> {
    List<GroupMember> findByUser(User user);
    List<GroupMember> findByGroup(Group group);
    boolean existsByUserAndGroup(User user, Group group);
    Optional<GroupMember> findByUserAndGroup(User user, Group group);
}
