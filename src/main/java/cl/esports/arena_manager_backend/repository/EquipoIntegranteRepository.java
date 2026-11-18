package cl.esports.arena_manager_backend.repository;

import cl.esports.arena_manager_backend.model.EquipoIntegrante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EquipoIntegranteRepository extends JpaRepository<EquipoIntegrante, Long> {

    @Query("SELECT COUNT(ei) > 0 FROM EquipoIntegrante ei WHERE ei.equipo.id = :equipoId AND ei.usuario.id = :usuarioId")
    boolean existsByEquipoIdAndUsuarioId(@Param("equipoId") Long equipoId, @Param("usuarioId") Long usuarioId);
}