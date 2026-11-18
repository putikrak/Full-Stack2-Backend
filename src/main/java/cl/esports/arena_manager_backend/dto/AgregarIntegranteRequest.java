package cl.esports.arena_manager_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AgregarIntegranteRequest {
    @NotNull(message = "El ID del jugador es obligatorio.")
    private Long usuarioId;

    private String rolEquipo;
}