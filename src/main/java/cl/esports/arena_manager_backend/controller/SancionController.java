package cl.esports.arena_manager_backend.controller;

import cl.esports.arena_manager_backend.dto.SancionRequest;
import cl.esports.arena_manager_backend.model.Sancion;
import cl.esports.arena_manager_backend.service.SancionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sanciones")
@RequiredArgsConstructor
@Tag(name = "7. Sanciones", description = "Gestión disciplinaria de jugadores")
public class SancionController {

    private final SancionService sancionService;

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Registrar una sanción disciplinaria vigente (ADMINISTRADOR / ORGANIZADOR)")
    public ResponseEntity<Sancion> crear(@Valid @RequestBody SancionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sancionService.crear(request));
    }
}