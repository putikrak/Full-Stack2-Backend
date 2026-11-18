package cl.esports.arena_manager_backend.security;

public record UsuarioAutenticado(Long id, String correo, String apodo, String rol) {
}