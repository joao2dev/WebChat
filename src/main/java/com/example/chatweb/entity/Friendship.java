package com.example.chatweb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;



@Entity
@Table(name = "friendships",uniqueConstraints = @UniqueConstraint(columnNames = {"requester_id","addressee_id" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Friendship {
    public enum FriendshipStatus {
        ACCEPTED,PENDING
    }
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "requester_id",nullable = false)
    private User requester;
    @ManyToOne
    @JoinColumn(name = "addressee_id",nullable = false)
    private User addressee;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendshipStatus status;
    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;
}
