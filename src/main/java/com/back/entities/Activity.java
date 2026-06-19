package com.back.entities;
import com.back.enums.ActivityAction;
import com.back.enums.ActivityResourceType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Activity {

    @Id
    @GeneratedValue
    private Long id;

    private ActivityAction action;

    private ActivityResourceType resource;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
