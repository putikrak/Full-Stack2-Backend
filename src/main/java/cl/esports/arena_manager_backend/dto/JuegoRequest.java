package cl.esports.arena_manager_backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JuegoRequest {
    @NotBlank(message = "El nombre del juego es obligatorio.")
    private String nombre;

    @NotNull(message = "La cantidad de integrantes por equipo es obligatoria.")
    @Min(value = 1, message = "Debe haber al menos 1 integrante por equipo.")
    private Integer integrantesPorEquipo;

    @NotBlank(message = "La modalidad es obligatoria (Equipos o Individual).")
    private String modalidad;
}