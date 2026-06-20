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
@Table(name = "group_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "sender_id",nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime sentAt;

    private LocalDateTime editedAt;

}
