package cl.esports.arena_manager_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InscripcionRequest {
    @NotNull(message = "El ID del torneo es obligatorio.")
    private Long torneoId;

    private Long equipoId;
    private Long usuarioId;
}