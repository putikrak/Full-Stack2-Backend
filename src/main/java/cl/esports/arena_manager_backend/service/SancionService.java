package cl.esports.arena_manager_backend.service;

import cl.esports.arena_manager_backend.config.ApiException;
import cl.esports.arena_manager_backend.dto.SancionRequest;
import cl.esports.arena_manager_backend.model.Sancion;
import cl.esports.arena_manager_backend.model.Usuario;
import cl.esports.arena_manager_backend.repository.SancionRepository;
import cl.esports.arena_manager_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SancionService {

    private final SancionRepository sancionRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Sancion crear(SancionRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + request.getUsuarioId()));

        Sancion sancion = Sancion.builder()
                .usuario(usuario)
                .motivo(request.getMotivo().trim())
                .duracion(request.getDuracion().trim())
                .vigente(true)
                .build();

        return sancionRepository.save(sancion);
    }
}