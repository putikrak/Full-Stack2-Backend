package cl.esports.arena_manager_backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TorneoRequest {
    @NotBlank(message = "El nombre del torneo es obligatorio.")
    private String nombre;

    @NotNull(message = "El ID del juego es obligatorio.")
    private Long juegoId;

    private String estado;

    @NotBlank(message = "La modalidad es obligatoria.")
    private String modalidad;

    @NotNull(message = "El cupo máximo es obligatorio.")
    @Min(value = 2, message = "El cupo máximo debe ser al menos 2.")
    private Integer cupoMaximo;

    @NotNull(message = "La fecha de inicio es obligatoria.")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de cierre de inscripción es obligatoria.")
    private LocalDate fechaCierreInscripcion;

    private String descripcion;
}