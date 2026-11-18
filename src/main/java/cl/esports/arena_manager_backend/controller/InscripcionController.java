package cl.esports.arena_manager_backend.controller;

import cl.esports.arena_manager_backend.dto.InscripcionRequest;
import cl.esports.arena_manager_backend.model.Inscripcion;
import cl.esports.arena_manager_backend.service.InscripcionService;
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
@RequestMapping("/api/v1/inscripciones")
@RequiredArgsConstructor
@Tag(name = "5. Inscripciones", description = "Registro y auditoría de inscripciones a torneos")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar todas las inscripciones (ADMINISTRADOR / ORGANIZADOR)")
    public ResponseEntity<List<Inscripcion>> listar() {
        return ResponseEntity.ok(inscripcionService.listarTodas());
    }

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Inscribir equipo o jugador en un torneo abierto")
    public ResponseEntity<Inscripcion> inscribir(@Valid @RequestBody InscripcionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inscripcionService.inscribir(request));
    }
}