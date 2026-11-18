package cl.esports.arena_manager_backend.service;

import cl.esports.arena_manager_backend.config.ApiException;
import cl.esports.arena_manager_backend.dto.ActualizarJugadorRequest;
import cl.esports.arena_manager_backend.model.Usuario;
import cl.esports.arena_manager_backend.repository.UsuarioRepository;
import cl.esports.arena_manager_backend.security.UsuarioAutenticado;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public Usuario actualizarPerfil(Long id, ActualizarJugadorRequest request, UsuarioAutenticado actual) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Jugador no encontrado con ID: " + id));

        boolean esAdmin = actual != null && "ADMINISTRADOR".equalsIgnoreCase(actual.rol());
        boolean esPropio = actual != null && actual.id().equals(id);

        if (!esAdmin && !esPropio) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Solo puedes modificar tu propio perfil.");
        }

        if (request.getApodo() != null && !request.getApodo().isBlank()) {
            String nuevoApodo = request.getApodo().trim();
            if (nuevoApodo.contains(" ") || nuevoApodo.length() < 3 || nuevoApodo.length() > 15) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "El apodo debe tener entre 3 y 15 caracteres sin espacios.");
            }
            if (!nuevoApodo.equalsIgnoreCase(usuario.getApodo()) && usuarioRepository.existsByApodoIgnoreCase(nuevoApodo)) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Ese apodo ya está en uso.");
            }
            usuario.setApodo(nuevoApodo);
        }

        if (request.getCorreo() != null && !request.getCorreo().isBlank()) {
            String nuevoCorreo = request.getCorreo().trim();
            if (!nuevoCorreo.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Formato de correo electrónico inválido.");
            }
            if (!nuevoCorreo.equalsIgnoreCase(usuario.getCorreo()) && usuarioRepository.existsByCorreoIgnoreCase(nuevoCorreo)) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Ese correo ya está registrado.");
            }
            usuario.setCorreo(nuevoCorreo);
        }

        if (request.getEdad() != null) {
            if (request.getEdad() < 13 || request.getEdad() > 99) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "La edad debe estar entre 13 y 99 años.");
            }
            usuario.setEdad(request.getEdad());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            if (request.getPassword().length() < 8) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "La contraseña debe tener al menos 8 caracteres.");
            }
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return usuarioRepository.save(usuario);
    }
}