package cl.esports.arena_manager_backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistroRequest {
    @NotBlank(message = "El nombre es obligatorio.")
    private String nombre;

    @NotBlank(message = "El apodo es obligatorio.")
    @Size(min = 3, max = 30, message = "El apodo debe tener entre 3 y 30 caracteres.")
    private String apodo;

    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "Formato de correo inválido.")
    private String correo;

    @NotNull(message = "La edad es obligatoria.")
    @Min(value = 13, message = "La edad mínima es 13 años.")
    @Max(value = 99, message = "La edad máxima es 99 años.")
    private Integer edad;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    private String password;
}