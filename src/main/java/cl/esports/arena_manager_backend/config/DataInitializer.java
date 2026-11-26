package cl.esports.arena_manager_backend.config;

import cl.esports.arena_manager_backend.model.*;
import cl.esports.arena_manager_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final JuegoRepository juegoRepository;
    private final EquipoRepository equipoRepository;
    private final EquipoIntegranteRepository integranteRepository;
    private final TorneoRepository torneoRepository;
    private final InscripcionRepository inscripcionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0 || juegoRepository.count() > 0) {
            return;
        }

        // 1. Usuarios iniciales (contraseña común: Arena1234!)
        String clave = passwordEncoder.encode("Arena1234!");

        Usuario admin = usuarioRepository.save(Usuario.builder()
                .nombre("Administrador Arena")
                .apodo("AdminArena")
                .correo("admin@arena.gg")
                .edad(28)
                .password(clave)
                .rol("ADMINISTRADOR")
                .victorias(0)
                .derrotas(0)
                .build());

        Usuario organizador = usuarioRepository.save(Usuario.builder()
                .nombre("Camila Torres")
                .apodo("CamiCaster")
                .correo("organizador@arena.gg")
                .edad(25)
                .password(clave)
                .rol("ORGANIZADOR")
                .victorias(4)
                .derrotas(2)
                .build());

        Usuario j1 = usuarioRepository.save(Usuario.builder()
                .nombre("Matías Rojas")
                .apodo("ShadowClaw")
                .correo("matias@arena.gg")
                .edad(21)
                .password(clave)
                .rol("JUGADOR")
                .victorias(18)
                .derrotas(6)
                .build());

        Usuario j2 = usuarioRepository.save(Usuario.builder()
                .nombre("Valentina Soto")
                .apodo("Valkyria")
                .correo("valentina@arena.gg")
                .edad(20)
                .password(clave)
                .rol("JUGADOR")
                .victorias(15)
                .derrotas(7)
                .build());

        Usuario j3 = usuarioRepository.save(Usuario.builder()
                .nombre("Diego Muñoz")
                .apodo("KronoZ")
                .correo("diego@arena.gg")
                .edad(22)
                .password(clave)
                .rol("JUGADOR")
                .victorias(12)
                .derrotas(9)
                .build());

        // 2. Catálogo de Juegos
        Juego valorant = juegoRepository.save(Juego.builder()
                .nombre("Valorant")
                .integrantesPorEquipo(5)
                .modalidad("Equipos")
                .build());

        Juego lol = juegoRepository.save(Juego.builder()
                .nombre("League of Legends")
                .integrantesPorEquipo(5)
                .modalidad("Equipos")
                .build());

        Juego cs2 = juegoRepository.save(Juego.builder()
                .nombre("Counter-Strike 2")
                .integrantesPorEquipo(5)
                .modalidad("Equipos")
                .build());

        Juego sf6 = juegoRepository.save(Juego.builder()
                .nombre("Street Fighter 6")
                .integrantesPorEquipo(1)
                .modalidad("Individual")
                .build());

        // 3. Equipos e Integrantes
        Equipo teamLeviatan = equipoRepository.save(Equipo.builder()
                .nombre("Leviatán Austral")
                .activo(true)
                .juego(valorant)
                .capitan(j1)
                .build());

        integranteRepository.saveAll(List.of(
                EquipoIntegrante.builder().equipo(teamLeviatan).usuario(j1).rolEquipo("Capitán").build(),
                EquipoIntegrante.builder().equipo(teamLeviatan).usuario(j2).rolEquipo("Duelista").build(),
                EquipoIntegrante.builder().equipo(teamLeviatan).usuario(j3).rolEquipo("Controlador").build()
        ));

        // 4. Torneos
        Torneo copaVal = torneoRepository.save(Torneo.builder()
                .nombre("Copa Cordillera Valorant 2026")
                .juego(valorant)
                .estado("abierto")
                .modalidad("Equipos")
                .cupoMaximo(16)
                .cupoOcupado(1)
                .fechaInicio(LocalDate.now().plusDays(10))
                .fechaCierreInscripcion(LocalDate.now().plusDays(7))
                .descripcion("Torneo universitario oficial 5v5 en servidor Santiago.")
                .build());

        torneoRepository.save(Torneo.builder()
                .nombre("Liga Universitaria LoL Apertura")
                .juego(lol)
                .estado("abierto")
                .modalidad("Equipos")
                .cupoMaximo(8)
                .cupoOcupado(0)
                .fechaInicio(LocalDate.now().plusDays(15))
                .fechaCierreInscripcion(LocalDate.now().plusDays(12))
                .descripcion("Competencia regional Grieta del Invocador formato eliminación directa.")
                .build());

        Torneo dueloSf6 = torneoRepository.save(Torneo.builder()
                .nombre("Arena Fighting Championship SF6")
                .juego(sf6)
                .estado("abierto")
                .modalidad("Individual")
                .cupoMaximo(32)
                .cupoOcupado(1)
                .fechaInicio(LocalDate.now().plusDays(5))
                .fechaCierreInscripcion(LocalDate.now().plusDays(3))
                .descripcion("Llave individual 1v1 al mejor de 3 combates.")
                .build());

        // 5. Inscripciones iniciales
        inscripcionRepository.save(Inscripcion.builder()
                .torneo(copaVal)
                .equipo(teamLeviatan)
                .fechaInscripcion(LocalDateTime.now())
                .estado("CONFIRMADA")
                .build());

        inscripcionRepository.save(Inscripcion.builder()
                .torneo(dueloSf6)
                .usuario(j1)
                .fechaInscripcion(LocalDateTime.now())
                .estado("CONFIRMADA")
                .build());

        System.out.println(">>> [DataInitializer] Datos iniciales cargados exitosamente en MySQL.");
    }
}