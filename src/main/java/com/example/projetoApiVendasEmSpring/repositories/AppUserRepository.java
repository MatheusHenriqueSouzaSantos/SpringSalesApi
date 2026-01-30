package com.example.projetoApiVendasEmSpring.repositories;

import com.example.projetoApiVendasEmSpring.entities.AppUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends BaseRepository<AppUser,UUID>{
    @Query("""
        select appUser
        from AppUser appUser
        where appUser.id <> :systemUserId
        order by appUser.active desc
    """)
    public List<AppUser> findAllAppUserExceptSystemUser(@Param("systemId") UUID systemUserId);

    @Query("""
        select appUser
        from AppUser appUser
        where appUser.id = :id
        and appUser.id <> :systemUserId
    """)
    public Optional<AppUser> findAppUserByIdExceptSystemUser(@Param("systemUserId")UUID systemUserId, @Param("id") UUID id);

    @Query("""
        select appUser
        from AppUser appUser
        where appUser.id = :id
        and appUser.id <> :systemUserId
        and appUser.active=true
    """)
    public Optional<AppUser> findActiveAppUserByIdExceptSystemUser(@Param("systemUserId")UUID systemUserId, @Param("id") UUID id);

    @Query("""
        select appUser
        from AppUser appUser
        where appUser.email = :email
        and appUser.id <> :systemUserId
    """)
    public Optional<AppUser> findAppUserByEmailExceptSystemUser(@Param("systemUserId")UUID systemUserId, @Param("email") String email);

    @Query("""
        select
        case
        when count(appUser)>0 then true
        else false
        end
        from AppUser appUser
        where appUser.email = :email
    """)
    public boolean verifyExistenceAppUserByEmail(@Param("email")String email);
    

}
