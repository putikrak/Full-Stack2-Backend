package cl.esports.arena_manager_backend.service;

import cl.esports.arena_manager_backend.config.ApiException;
import cl.esports.arena_manager_backend.dto.JuegoRequest;
import cl.esports.arena_manager_backend.model.Juego;
import cl.esports.arena_manager_backend.repository.JuegoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JuegoService {

    private final JuegoRepository juegoRepository;

    @Transactional(readOnly = true)
    public List<Juego> listarTodos() {
        return juegoRepository.findAll();
    }

    @Transactional
    public Juego crear(JuegoRequest request) {
        if (juegoRepository.existsByNombreIgnoreCase(request.getNombre().trim())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Ya existe un juego con ese nombre.");
        }
        Juego juego = Juego.builder()
                .nombre(request.getNombre().trim())
                .integrantesPorEquipo(request.getIntegrantesPorEquipo())
                .modalidad(request.getModalidad().trim())
                .build();
        return juegoRepository.save(juego);
    }
}