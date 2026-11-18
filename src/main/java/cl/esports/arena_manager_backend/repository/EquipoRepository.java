package cl.esports.arena_manager_backend.repository;

import cl.esports.arena_manager_backend.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
}