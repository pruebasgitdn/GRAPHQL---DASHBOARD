package com.back.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@ToString(exclude = {"members", "owner_id","projects"})
@Getter
@Setter
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String color;

    // R 1:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;


    //R 1:N
    @Builder.Default
    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<WorkspaceMember> members = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Project> projects = new ArrayList<>();


    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


}
