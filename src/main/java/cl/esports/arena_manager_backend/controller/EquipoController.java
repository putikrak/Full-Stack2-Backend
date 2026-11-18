package cl.esports.arena_manager_backend.controller;

import cl.esports.arena_manager_backend.dto.AgregarIntegranteRequest;
import cl.esports.arena_manager_backend.dto.EquipoRequest;
import cl.esports.arena_manager_backend.model.Equipo;
import cl.esports.arena_manager_backend.service.EquipoService;
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
@RequestMapping("/api/v1/equipos")
@RequiredArgsConstructor
@Tag(name = "4. Equipos", description = "Creación de equipos y administración de plantillas")
public class EquipoController {

    private final EquipoService equipoService;

    @GetMapping
    @Operation(summary = "Listar equipos y sus integrantes (Pública)")
    public ResponseEntity<List<Equipo>> listar() {
        return ResponseEntity.ok(equipoService.listarTodos());
    }

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Crear un nuevo equipo (JUGADOR / ADMINISTRADOR)")
    public ResponseEntity<Equipo> crear(@Valid @RequestBody EquipoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(equipoService.crear(request));
    }

    @PostMapping("/{id}/integrantes")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Agregar jugador a la plantilla del equipo")
    public ResponseEntity<Equipo> agregarIntegrante(
            @PathVariable Long id,
            @Valid @RequestBody AgregarIntegranteRequest request) {
        return ResponseEntity.ok(equipoService.agregarIntegrante(id, request));
    }

    @DeleteMapping("/{id}/integrantes/{usuarioId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Quitar jugador de la plantilla del equipo")
    public ResponseEntity<Equipo> quitarIntegrante(
            @PathVariable Long id,
            @PathVariable Long usuarioId) {
        return ResponseEntity.ok(equipoService.quitarIntegrante(id, usuarioId));
    }
}