package cl.esports.arena_manager_backend.dto;

import lombok.Data;

@Data
public class ActualizarJugadorRequest {
    private String apodo;
    private String correo;
    private Integer edad;
    private String password;
}