package cl.esports.arena_manager_backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TorneoUpdateRequest {
    private String nombre;
    private Long juegoId;
    private String estado;
    private String modalidad;
    private Integer cupoMaximo;
    private LocalDate fechaInicio;
    private LocalDate fechaCierreInscripcion;
    private String descripcion;
}