package com.example.projetoApiVendasEmSpring.entities;

import com.example.projetoApiVendasEmSpring.services.Utils;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEntity {

    @Id
    private UUID id;

    @Column(name = "created_at",nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name="updated_at", nullable =true)
    @Setter
    private Instant updatedAt=null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false,updatable = false)
    private AppUser createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_id", nullable = true)
    @Setter
    private AppUser updatedBy=null;

    @Column(name = "is_active", nullable = false)
    @Setter
    private boolean isActive=true;

    @PrePersist
    public void prePersist(){
        id= Utils.GenerateRadomUUID();
        this.createdAt=Instant.now();
        onPrePersist();
    }
    @PreUpdate
    public void preUpdate(){
        this.updatedAt=Instant.now();
    }

    protected void onPrePersist(){

    }

    public BaseEntity(AppUser createdBy) {
        this.createdBy = createdBy;
    }
}
