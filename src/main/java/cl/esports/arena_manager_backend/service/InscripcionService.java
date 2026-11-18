package cl.esports.arena_manager_backend.service;

import cl.esports.arena_manager_backend.config.ApiException;
import cl.esports.arena_manager_backend.dto.InscripcionRequest;
import cl.esports.arena_manager_backend.model.*;
import cl.esports.arena_manager_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final TorneoRepository torneoRepository;
    private final EquipoRepository equipoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SancionRepository sancionRepository;

    @Transactional(readOnly = true)
    public List<Inscripcion> listarTodas() {
        return inscripcionRepository.findAll();
    }

    @Transactional
    public Inscripcion inscribir(InscripcionRequest request) {
        Torneo torneo = torneoRepository.findById(request.getTorneoId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Torneo no encontrado."));

        if (!"abierto".equalsIgnoreCase(torneo.getEstado())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El torneo no se encuentra abierto para inscripciones.");
        }

        if (LocalDate.now().isAfter(torneo.getFechaCierreInscripcion())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El plazo de inscripción para este torneo ya cerró.");
        }

        if (torneo.getCupoOcupado() >= torneo.getCupoMaximo()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El torneo ya alcanzó su cupo máximo de participantes.");
        }

        if (request.getEquipoId() == null && request.getUsuarioId() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Debes indicar un equipo o un jugador para inscribir.");
        }

        Equipo equipo = null;
        Usuario usuario = null;

        if (request.getEquipoId() != null) {
            equipo = equipoRepository.findById(request.getEquipoId())
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Equipo no encontrado."));

            if (!Boolean.TRUE.equals(equipo.getActivo())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "El equipo seleccionado se encuentra inactivo.");
            }

            if (inscripcionRepository.existsByTorneoIdAndEquipoId(torneo.getId(), equipo.getId())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Este equipo ya está inscrito en el torneo.");
            }

            int requeridos = torneo.getJuego().getIntegrantesPorEquipo();
            if (equipo.getIntegrantes().size() < requeridos) {
                throw new ApiException(HttpStatus.BAD_REQUEST,
                        "Nómina incompleta: el equipo requiere al menos " + requeridos + " integrantes.");
            }

            if (sancionRepository.existsByUsuarioIdAndVigenteTrue(equipo.getCapitan().getId())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "El capitán del equipo tiene una sanción disciplinaria vigente.");
            }

            for (EquipoIntegrante miembro : equipo.getIntegrantes()) {
                if (sancionRepository.existsByUsuarioIdAndVigenteTrue(miembro.getUsuario().getId())) {
                    throw new ApiException(HttpStatus.BAD_REQUEST,
                            "El integrante " + miembro.getUsuario().getApodo() + " posee una sanción vigente.");
                }
            }
        } else {
            usuario = usuarioRepository.findById(request.getUsuarioId())
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Jugador no encontrado."));

            if (inscripcionRepository.existsByTorneoIdAndUsuarioId(torneo.getId(), usuario.getId())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Este jugador ya está inscrito en el torneo.");
            }

            if (sancionRepository.existsByUsuarioIdAndVigenteTrue(usuario.getId())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "El jugador posee una sanción disciplinaria vigente.");
            }
        }

        torneo.setCupoOcupado(torneo.getCupoOcupado() + 1);
        torneoRepository.save(torneo);

        Inscripcion inscripcion = Inscripcion.builder()
                .torneo(torneo)
                .equipo(equipo)
                .usuario(usuario)
                .fechaInscripcion(LocalDateTime.now())
                .estado("CONFIRMADA")
                .build();

        return inscripcionRepository.save(inscripcion);
    }
}