package com.example.projetoApiVendasEmSpring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T,P> extends JpaRepository<T,P> {
     List<T> findByActiveTrue();

     List<T> findAllOrderByActiveDesc();

     Optional<T> findByIdAndActiveTrue(P id);

}
