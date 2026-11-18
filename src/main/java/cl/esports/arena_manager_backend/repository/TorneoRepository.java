package cl.esports.arena_manager_backend.repository;

import cl.esports.arena_manager_backend.model.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TorneoRepository extends JpaRepository<Torneo, Long> {
}