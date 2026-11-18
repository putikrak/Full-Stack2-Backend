package cl.esports.arena_manager_backend.service;

import cl.esports.arena_manager_backend.config.ApiException;
import cl.esports.arena_manager_backend.dto.AgregarIntegranteRequest;
import cl.esports.arena_manager_backend.dto.EquipoRequest;
import cl.esports.arena_manager_backend.model.Equipo;
import cl.esports.arena_manager_backend.model.EquipoIntegrante;
import cl.esports.arena_manager_backend.model.Juego;
import cl.esports.arena_manager_backend.model.Usuario;
import cl.esports.arena_manager_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipoService {

    private final EquipoRepository equipoRepository;
    private final EquipoIntegranteRepository integranteRepository;
    private final JuegoRepository juegoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SancionRepository sancionRepository;

    @Transactional(readOnly = true)
    public List<Equipo> listarTodos() {
        return equipoRepository.findAll();
    }

    @Transactional
    public Equipo crear(EquipoRequest request) {
        if (equipoRepository.existsByNombreIgnoreCase(request.getNombre().trim())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Ya existe un equipo con ese nombre.");
        }

        Juego juego = juegoRepository.findById(request.getJuegoId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "El juego seleccionado no existe."));

        if (!"Equipos".equalsIgnoreCase(juego.getModalidad())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Debes seleccionar un juego con modalidad por equipos.");
        }

        Usuario capitan = usuarioRepository.findById(request.getCapitanId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "El capitán seleccionado no existe."));

        if (sancionRepository.existsByUsuarioIdAndVigenteTrue(capitan.getId())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El capitán tiene una sanción vigente y no puede crear el equipo.");
        }

        Equipo equipo = Equipo.builder()
                .nombre(request.getNombre().trim())
                .juego(juego)
                .capitan(capitan)
                .activo(true)
                .build();

        Equipo guardado = equipoRepository.save(equipo);

        EquipoIntegrante registroCapitan = EquipoIntegrante.builder()
                .equipo(guardado)
                .usuario(capitan)
                .rolEquipo("Capitán")
                .build();

        integranteRepository.save(registroCapitan);
        guardado.getIntegrantes().add(registroCapitan);

        return guardado;
    }

    @Transactional
    public Equipo agregarIntegrante(Long equipoId, AgregarIntegranteRequest request) {
        Equipo equipo = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Equipo no encontrado con ID: " + equipoId));

        Usuario jugador = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "El jugador seleccionado no existe."));

        if (sancionRepository.existsByUsuarioIdAndVigenteTrue(jugador.getId())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El jugador posee una sanción vigente y no puede ser agregado.");
        }

        if (integranteRepository.existsByEquipoIdAndUsuarioId(equipoId, jugador.getId())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El jugador ya pertenece a la plantilla de este equipo.");
        }

        int maximo = equipo.getJuego().getIntegrantesPorEquipo();
        if (equipo.getIntegrantes().size() >= maximo) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "La nómina máxima para " + equipo.getJuego().getNombre() + " es de " + maximo + " jugadores.");
        }

        String rol = (request.getRolEquipo() == null || request.getRolEquipo().isBlank())
                ? "Titular"
                : request.getRolEquipo().trim();

        EquipoIntegrante nuevo = EquipoIntegrante.builder()
                .equipo(equipo)
                .usuario(jugador)
                .rolEquipo(rol)
                .build();

        integranteRepository.save(nuevo);
        equipo.getIntegrantes().add(nuevo);
        return equipo;
    }

    @Transactional
    public Equipo quitarIntegrante(Long equipoId, Long usuarioId) {
        Equipo equipo = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Equipo no encontrado con ID: " + equipoId));

        if (equipo.getCapitan().getId().equals(usuarioId)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "No se puede eliminar al capitán del equipo.");
        }

        EquipoIntegrante integrante = equipo.getIntegrantes().stream()
                .filter(i -> i.getUsuario().getId().equals(usuarioId))
                .findFirst()
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "El jugador no forma parte de este equipo."));

        equipo.getIntegrantes().remove(integrante);
        integranteRepository.delete(integrante);
        return equipo;
    }
}