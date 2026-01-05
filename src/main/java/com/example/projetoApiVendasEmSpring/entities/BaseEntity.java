package com.example.projetoApiVendasEmSpring.entities;

import com.example.projetoApiVendasEmSpring.services.Utils;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEntity {

    @Id
    @Getter
    private UUID id;
    @Getter
    @Column(name = "created_at",nullable = false, updatable = false)
    private Instant createdAt;
    @Getter
    @Column(name="updated_at", nullable =true)
    @Setter
    private Instant updatedAt=null;
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false,updatable = false)
    private AppUser createdBy;
    @Getter
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

    public boolean getIsActive() {
        return isActive;
    }
}
