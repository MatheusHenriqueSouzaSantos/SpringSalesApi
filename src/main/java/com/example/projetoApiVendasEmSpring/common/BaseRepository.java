package com.example.projetoApiVendasEmSpring.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T,P> extends JpaRepository<T,P> {
     List<T> findByActiveTrue();

     List<T> findAllByOrderByActiveDesc();

     Optional<T> findByIdAndActiveTrue(P id);

}
