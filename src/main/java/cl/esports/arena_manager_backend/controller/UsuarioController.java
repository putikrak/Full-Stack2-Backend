package cl.esports.arena_manager_backend.controller;

import cl.esports.arena_manager_backend.dto.ActualizarJugadorRequest;
import cl.esports.arena_manager_backend.model.Usuario;
import cl.esports.arena_manager_backend.security.UsuarioAutenticado;
import cl.esports.arena_manager_backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jugadores")
@RequiredArgsConstructor
@Tag(name = "6. Jugadores", description = "Fichas competitivas y edición de perfil")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar fichas competitivas de jugadores (Pública)")
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Actualizar datos de perfil o contraseña de un jugador")
    public ResponseEntity<Usuario> actualizar(
            @PathVariable Long id,
            @RequestBody ActualizarJugadorRequest request,
            @AuthenticationPrincipal UsuarioAutenticado actual) {
        return ResponseEntity.ok(usuarioService.actualizarPerfil(id, request, actual));
    }
}