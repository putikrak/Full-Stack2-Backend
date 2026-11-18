package cl.esports.arena_manager_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SancionRequest {
    @NotNull(message = "El ID del usuario sancionado es obligatorio.")
    private Long usuarioId;

    @NotBlank(message = "El motivo de la sanción es obligatorio.")
    private String motivo;

    @NotBlank(message = "La duración de la sanción es obligatoria.")
    private String duracion;
}