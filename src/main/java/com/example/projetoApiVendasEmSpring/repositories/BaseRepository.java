package com.example.projetoApiVendasEmSpring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BaseRepository<T,P> extends JpaRepository<T,P> {
    public List<T> findByIsActiveTrue();

    public Optional<T> findByIdAndIsActiveTrue(P id);
}
