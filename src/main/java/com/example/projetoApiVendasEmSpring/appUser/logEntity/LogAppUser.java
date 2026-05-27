//package com.example.projetoApiVendasEmSpring.appUser.logEntity;
//
//import com.example.projetoApiVendasEmSpring.appUser.entity.AppUser;
//import jakarta.persistence.*;
//import lombok.Getter;
//
//import java.time.Instant;
//import java.util.UUID;
//
//@Entity
//@Table(name = "log_app_user")
//@Getter
//public class LogAppUser {
//    @GeneratedValue(strategy = GenerationType.UUID)
//    @Id
//    private UUID id;
//    @ManyToOne
//    @JoinColumn(name = "app_user_id", updatable = false, nullable = false)
//    private AppUser appUser;
//    @Column(name = "field",updatable = false,nullable = false)
//    private String field;
//    @Column(name = "old_value", updatable = false, nullable = false)
//    private String oldValue;
//    @Column(name = "new_value", updatable = false, nullable = false)
//    private String newValue;
//    @ManyToOne
//    @JoinColumn(name = "changed_by_app_user_id",updatable = false,nullable = false)
//    private AppUser changedByAppUser;
//    @Column(name = "updated_at",updatable = false,nullable = false)
//    private Instant updatedAt;
//
//    public LogAppUser(AppUser appUser, String field, String oldValue, String newValue, AppUser changedByAppUser) {
//        this.appUser = appUser;
//        this.field = field;
//        this.oldValue = oldValue;
//        this.newValue = newValue;
//        this.changedByAppUser = changedByAppUser;
//    }
//    @PrePersist
//    protected void prePersist(){
//        updatedAt=Instant.now();
//    }
//}
