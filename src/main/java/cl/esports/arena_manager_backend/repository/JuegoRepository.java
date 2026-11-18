package cl.esports.arena_manager_backend.repository;

import cl.esports.arena_manager_backend.model.Juego;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JuegoRepository extends JpaRepository<Juego, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
}