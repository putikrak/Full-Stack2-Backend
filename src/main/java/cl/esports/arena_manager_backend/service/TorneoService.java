package cl.esports.arena_manager_backend.service;

import cl.esports.arena_manager_backend.config.ApiException;
import cl.esports.arena_manager_backend.dto.TorneoRequest;
import cl.esports.arena_manager_backend.dto.TorneoUpdateRequest;
import cl.esports.arena_manager_backend.model.Juego;
import cl.esports.arena_manager_backend.model.Torneo;
import cl.esports.arena_manager_backend.repository.JuegoRepository;
import cl.esports.arena_manager_backend.repository.TorneoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TorneoService {

    private final TorneoRepository torneoRepository;
    private final JuegoRepository juegoRepository;

    @Transactional(readOnly = true)
    public List<Torneo> listar(Long juegoId, String estado, String busqueda, LocalDate desde, LocalDate hasta) {
        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La fecha inicial no puede ser posterior a la fecha final.");
        }

        return torneoRepository.findAll().stream()
                .filter(t -> juegoId == null || (t.getJuego() != null && t.getJuego().getId().equals(juegoId)))
                .filter(t -> estado == null || estado.isBlank() || t.getEstado().equalsIgnoreCase(estado.trim()))
                .filter(t -> busqueda == null || busqueda.isBlank()
                        || t.getNombre().toLowerCase().contains(busqueda.trim().toLowerCase()))
                .filter(t -> desde == null || !t.getFechaInicio().isBefore(desde))
                .filter(t -> hasta == null || !t.getFechaInicio().isAfter(hasta))
                .toList();
    }

    @Transactional(readOnly = true)
    public Torneo obtenerPorId(Long id) {
        return torneoRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Torneo no encontrado con ID: " + id));
    }

    @Transactional
    public Torneo crear(TorneoRequest request) {
        if (request.getFechaCierreInscripcion().isAfter(request.getFechaInicio())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La fecha de cierre debe ser anterior o igual a la fecha de inicio.");
        }

        Juego juego = juegoRepository.findById(request.getJuegoId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "El juego indicado no existe."));

        String estadoInicial = (request.getEstado() == null || request.getEstado().isBlank())
                ? "abierto"
                : request.getEstado().trim().toLowerCase();

        Torneo torneo = Torneo.builder()
                .nombre(request.getNombre().trim())
                .juego(juego)
                .estado(estadoInicial)
                .modalidad(request.getModalidad().trim())
                .cupoMaximo(request.getCupoMaximo())
                .cupoOcupado(0)
                .fechaInicio(request.getFechaInicio())
                .fechaCierreInscripcion(request.getFechaCierreInscripcion())
                .descripcion(request.getDescripcion())
                .build();

        return torneoRepository.save(torneo);
    }

    @Transactional
    public Torneo actualizar(Long id, TorneoUpdateRequest request) {
        Torneo torneo = obtenerPorId(id);

        if (request.getNombre() != null && !request.getNombre().isBlank()) {
            torneo.setNombre(request.getNombre().trim());
        }
        if (request.getJuegoId() != null) {
            Juego juego = juegoRepository.findById(request.getJuegoId())
                    .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "El juego indicado no existe."));
            torneo.setJuego(juego);
        }
        if (request.getEstado() != null && !request.getEstado().isBlank()) {
            torneo.setEstado(request.getEstado().trim().toLowerCase());
        }
        if (request.getModalidad() != null && !request.getModalidad().isBlank()) {
            torneo.setModalidad(request.getModalidad().trim());
        }
        if (request.getCupoMaximo() != null) {
            if (request.getCupoMaximo() < torneo.getCupoOcupado()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "El cupo máximo no puede ser menor a los cupos ya ocupados.");
            }
            torneo.setCupoMaximo(request.getCupoMaximo());
        }
        if (request.getFechaInicio() != null) {
            torneo.setFechaInicio(request.getFechaInicio());
        }
        if (request.getFechaCierreInscripcion() != null) {
            torneo.setFechaCierreInscripcion(request.getFechaCierreInscripcion());
        }
        if (torneo.getFechaCierreInscripcion().isAfter(torneo.getFechaInicio())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La fecha de cierre debe ser anterior o igual a la fecha de inicio.");
        }
        if (request.getDescripcion() != null) {
            torneo.setDescripcion(request.getDescripcion());
        }

        return torneoRepository.save(torneo);
    }

    @Transactional
    public void eliminar(Long id) {
        Torneo torneo = obtenerPorId(id);
        torneoRepository.delete(torneo);
    }
}
