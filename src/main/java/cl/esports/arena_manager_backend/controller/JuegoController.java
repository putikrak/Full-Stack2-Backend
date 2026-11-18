package cl.esports.arena_manager_backend.controller;

import cl.esports.arena_manager_backend.dto.JuegoRequest;
import cl.esports.arena_manager_backend.model.Juego;
import cl.esports.arena_manager_backend.service.JuegoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/juegos")
@RequiredArgsConstructor
@Tag(name = "2. Juegos", description = "Catálogo de videojuegos competitivos")
public class JuegoController {

    private final JuegoService juegoService;

    @GetMapping
    @Operation(summary = "Listar catálogo completo de juegos (Pública)")
    public ResponseEntity<List<Juego>> listar() {
        return ResponseEntity.ok(juegoService.listarTodos());
    }

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Agregar un nuevo juego (Solo ADMINISTRADOR)")
    public ResponseEntity<Juego> crear(@Valid @RequestBody JuegoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(juegoService.crear(request));
    }
}