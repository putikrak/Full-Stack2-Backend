package cl.esports.arena_manager_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EquipoRequest {
    @NotBlank(message = "El nombre del equipo es obligatorio.")
    @Size(min = 3, max = 80, message = "El nombre debe tener entre 3 y 80 caracteres.")
    private String nombre;

    @NotNull(message = "El juego principal es obligatorio.")
    private Long juegoId;

    @NotNull(message = "El capitán es obligatorio.")
    private Long capitanId;
}