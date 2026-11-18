package cl.esports.arena_manager_backend.controller;

import cl.esports.arena_manager_backend.dto.TorneoRequest;
import cl.esports.arena_manager_backend.dto.TorneoUpdateRequest;
import cl.esports.arena_manager_backend.model.Torneo;
import cl.esports.arena_manager_backend.service.TorneoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/torneos")
@RequiredArgsConstructor
@Tag(name = "3. Torneos", description = "Gestión y exploración de torneos eSports")
public class TorneoController {

    private final TorneoService torneoService;

    @GetMapping
    @Operation(summary = "Listar torneos con filtros opcionales (Pública)")
    public ResponseEntity<List<Torneo>> listar(
            @RequestParam(required = false) Long juegoId,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(torneoService.listar(juegoId, estado, busqueda, desde, hasta));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de un torneo por ID (Pública)")
    public ResponseEntity<Torneo> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(torneoService.obtenerPorId(id));
    }

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Crear un nuevo torneo (ADMINISTRADOR / ORGANIZADOR)")
    public ResponseEntity<Torneo> crear(@Valid @RequestBody TorneoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(torneoService.crear(request));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Actualizar datos o estado de un torneo (ADMINISTRADOR / ORGANIZADOR)")
    public ResponseEntity<Torneo> actualizar(@PathVariable Long id, @RequestBody TorneoUpdateRequest request) {
        return ResponseEntity.ok(torneoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Eliminar un torneo (Solo ADMINISTRADOR)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        torneoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}