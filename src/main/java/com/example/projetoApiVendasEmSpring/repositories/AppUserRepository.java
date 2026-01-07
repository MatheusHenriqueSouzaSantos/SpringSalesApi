package com.example.projetoApiVendasEmSpring.repositories;

import com.example.projetoApiVendasEmSpring.entities.AppUser;
import com.example.projetoApiVendasEmSpring.services.SystemUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends BaseRepository<AppUser,UUID>{
    @Query("""
        select appUser
        from AppUser appUser
        where appUser.isActive =true and
        appUser.id <> :systemUserId
    """)
    public List<AppUser> findAllAppUserIsActive(@Param("systemId") UUID systemUserId);

    @Query("""
        select appUser
        from AppUser appUser
        where appUser.id = :id
        and appUser.isActive=true
        and appUser.id <> :systemId
    """)
    public Optional<AppUser> findAppUserIsActiveById(@Param("systemId")UUID systemId, @Param("id") UUID id);
}
