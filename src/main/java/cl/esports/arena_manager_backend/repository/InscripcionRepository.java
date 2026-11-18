package cl.esports.arena_manager_backend.repository;

import cl.esports.arena_manager_backend.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    @Query("SELECT COUNT(i) > 0 FROM Inscripcion i WHERE i.torneo.id = :torneoId AND i.equipo.id = :equipoId")
    boolean existsByTorneoIdAndEquipoId(@Param("torneoId") Long torneoId, @Param("equipoId") Long equipoId);

    @Query("SELECT COUNT(i) > 0 FROM Inscripcion i WHERE i.torneo.id = :torneoId AND i.usuario.id = :usuarioId")
    boolean existsByTorneoIdAndUsuarioId(@Param("torneoId") Long torneoId, @Param("usuarioId") Long usuarioId);
}