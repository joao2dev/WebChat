package com.example.chatweb.Service;

import com.example.chatweb.entity.Group;
import com.example.chatweb.entity.GroupMember;
import com.example.chatweb.entity.User;
import com.example.chatweb.repositories.GroupMemberRepository;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class GroupMemberService {

    private final GroupMemberRepository repository;


    GroupMember addMember(User user, Group group){

        if (repository.existsByUserAndGroup(user, group)){
            throw new RuntimeException("o membro ja esta presente no grupo");
        }
        GroupMember newMember = new GroupMember(UUID.randomUUID(),user,group, LocalDateTime.now());
        return repository.save(newMember);
    }


    void removeMember(GroupMember member, User requester){
        if (!repository.existsByUserAndGroup(member.getUser(),member.getGroup())){
            throw new RuntimeException("esse membro nao existe");
        }
        boolean isOwner = requester.getId().equals(member.getGroup().getOwner().getId());
        boolean isSelf = requester.getId().equals(member.getUser().getId());

        if (!isOwner && !isSelf) {
            throw new RuntimeException("Voce nao e um administrador");
        }

        repository.deleteById(member.getId());
    }
    List<GroupMember> findMembers(Group group)   {
        return repository.findByGroup(group);

    }
}
