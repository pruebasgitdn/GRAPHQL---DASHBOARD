package com.back.entities;

import com.back.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Data
@Table(name = "WorkspaceMember",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id","workspace_id"})})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;


    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;
    @PrePersist
    protected void onCreate() { //Al ser creado
        joinedAt = LocalDateTime.now();
    }




}
