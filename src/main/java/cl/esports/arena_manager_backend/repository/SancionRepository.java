package cl.esports.arena_manager_backend.repository;

import cl.esports.arena_manager_backend.model.Sancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SancionRepository extends JpaRepository<Sancion, Long> {

    @Query("SELECT COUNT(s) > 0 FROM Sancion s WHERE s.usuario.id = :usuarioId AND s.vigente = true")
    boolean existsByUsuarioIdAndVigenteTrue(@Param("usuarioId") Long usuarioId);
}