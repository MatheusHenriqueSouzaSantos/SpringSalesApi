package com.example.projetoApiVendasEmSpring.common;

import com.example.projetoApiVendasEmSpring.appUser.entity.AppUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "created_at",nullable = false, updatable = false)
    private Instant createdAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false,updatable = false)
    private AppUser createdBy;
    @Column(name="updated_at", nullable =true)
    @Setter
    private Instant updatedAt=null;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_id", nullable = true)
    @Setter
    private AppUser updatedBy=null;
    @Column(name = "active", nullable = false)
    @Setter
    private boolean active =true;

    @PrePersist
    public void prePersist(){
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
